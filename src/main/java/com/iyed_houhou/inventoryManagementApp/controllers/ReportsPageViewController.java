package com.iyed_houhou.inventoryManagementApp.controllers;

import com.iyed_houhou.inventoryManagementApp.managers.SalesManager;
import com.iyed_houhou.inventoryManagementApp.models.Sale;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;

import java.io.FileWriter;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class ReportsPageViewController extends BasePageController{

    @FXML private ComboBox<String> reportTypeCombo;
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private TableView<Sale> salesTable;
    @FXML private TableColumn<Sale, Integer> colItemsCount;
    @FXML private BarChart<String, Number> salesChart;

    private final SalesManager salesManager = SalesManager.getInstance();
    private final ObservableList<Sale> salesData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        setupReportTypeCombo();
        configureTable();
        configureChart();
    }

    private void setupReportTypeCombo() {
        reportTypeCombo.getItems().addAll(
                "Daily Sales",
                "Weekly Sales",
                "Monthly Sales",
                "Custom Range"
        );
        reportTypeCombo.getSelectionModel().selectFirst();
    }

    private void configureTable() {
        // Configure other columns normally
        salesTable.setItems(salesData);

        // Configure items count column using direct method access
        colItemsCount.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(
                        cellData.getValue().getSaleItems().size()
                ).asObject()
        );

    }

    private void configureChart() {
        salesChart.getData().clear();
        salesChart.setLegendVisible(false);
    }

    @FXML
    private void generateReport() {
        LocalDate start = startDatePicker.getValue();
        LocalDate end = endDatePicker.getValue();
        String reportType = reportTypeCombo.getValue();

        // Handle report type logic
        if ("Custom Range".equals(reportType)) {
            if (start == null || end == null) {
                showAlert("custom Range Warrning","Please select both start and end dates for custom range", Alert.AlertType.WARNING);
                return;
            }
        } else {
            // Calculate dates based on report type
            LocalDate today = LocalDate.now();
            switch (reportType) {
                case "Daily Sales" -> {
                    start = today;
                    end = today;
                }
                case "Weekly Sales" -> {
                    start = today.minusWeeks(1);
                    end = today;
                }
                case "Monthly Sales" -> {
                    start = today.minusMonths(1);
                    end = today;
                }
            }
            // Update date pickers
            startDatePicker.setValue(start);
            endDatePicker.setValue(end);
        }

        List<Sale> allSales = salesManager.getAllSalesList(); // Ensure this method exists in SalesManager
        salesData.setAll(filterSalesByDate(allSales, start, end));
        // Configure items count column using direct method access
        colItemsCount.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(
                        cellData.getValue().getSaleItems().size()
                ).asObject()
        );
        updateChart();
    }

    private List<Sale> filterSalesByDate(List<Sale> sales, LocalDate start, LocalDate end) {
        return sales.stream()
                .filter(sale -> isDateInRange(sale.getSaleDate(), start, end))
                .toList();
    }

    private boolean isDateInRange(Date saleDate, LocalDate start, LocalDate end) {
        LocalDate date = saleDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return !date.isBefore(start) && !date.isAfter(end);
    }

    private void updateChart() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();

        salesData.forEach(sale -> {
            String date = sale.getSaleDate().toString();
            series.getData().add(new XYChart.Data<>(date, sale.getTotalAmount()));
        });

        salesChart.getData().clear();
        salesChart.getData().add(series);
    }

    @FXML
    private void exportToCSV() {
        try (FileWriter writer = new FileWriter("sales_report.csv")) {
            writer.write("Sale ID,Date,Total Amount,Items Count\n");

            for (Sale sale : salesData) {
                writer.write(String.format("%d,%s,%.2f,%d\n",
                        sale.getSaleId(),
                        sale.getSaleDate(),
                        sale.getTotalAmount(),
                        sale.getSaleItems().size()));
            }

            new Alert(Alert.AlertType.INFORMATION, "Exported to sales_report.csv").show();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Export failed: " + e.getMessage()).show();
        }
    }
}