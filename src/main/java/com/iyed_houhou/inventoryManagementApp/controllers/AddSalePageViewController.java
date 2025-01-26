package com.iyed_houhou.inventoryManagementApp.controllers;


import com.iyed_houhou.inventoryManagementApp.managers.ProductListManager;
import com.iyed_houhou.inventoryManagementApp.managers.SalesManager;
import com.iyed_houhou.inventoryManagementApp.models.*;
import com.iyed_houhou.inventoryManagementApp.utils.SessionManager;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;
import javafx.util.converter.IntegerStringConverter;

import java.util.Optional;
import java.util.function.Function;

public class AddSalePageViewController extends BasePageController {

    private class SafeIntegerStringConverter extends IntegerStringConverter {
        private Integer originalValue;

        public void setOriginalValue(Integer value) {
            this.originalValue = value;
        }

        @Override
        public Integer fromString(String value) {
            try {
                return super.fromString(value);
            } catch (NumberFormatException e) {
                showAlert("Invalid Input", "Please enter a valid integer number", Alert.AlertType.ERROR);
                return originalValue; // Return original value instead of throwing
            }
        }
    }

    @FXML
    private Label totalPriceLabel;
    @FXML
    private TableView<SaleItem> productsTable;
    @FXML
    private TableColumn<SaleItem, String> productNameColumn;
    @FXML
    private TableColumn<SaleItem, Integer> productQuantityColumn;
    @FXML
    private TableColumn<SaleItem, Double> productPriceColumn;
    @FXML
    private TableColumn<SaleItem, Double> productTotalColumn;
    @FXML
    private TableColumn<SaleItem, Void> actionsColumn; // Assuming this column will contain actions (buttons or something)

    @FXML
    private TextField addSaleItemField;
    private SalesManager salesManager;
    private ObservableList<SaleItem> saleItemList; // Change to ObservableList

    @FXML
    private void initialize() {
        User loggedInUser = SessionManager.getInstance().getLoggedInUser();
        if (loggedInUser == null || (!loggedInUser.getRole().equals(Role.Admin) && !loggedInUser.getRole().equals(Role.SuperAdmin)) ) {
            adminOnlyPage.setVisible(false);
        }
        salesManager = SalesManager.getInstance();
        saleItemList = FXCollections.observableArrayList(); // Initialize as ObservableList

        productsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        // Set up the TableView columns
        productNameColumn.setCellValueFactory(createStringPropertyCellValueFactory(item -> item.getProduct().getName()));
        productPriceColumn.setCellValueFactory(createDoublePropertyCellValueFactory(item -> item.getProduct().getSalePrice()));
        productTotalColumn.setCellValueFactory(createDoublePropertyCellValueFactory(item -> item.getTotalAmount().doubleValue()));

        // Configure the productQuantityColumn for editing
        productQuantityColumn.setCellValueFactory(createIntegerPropertyCellValueFactory(SaleItem::getQuantity));
        productQuantityColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        productQuantityColumn.setOnEditCommit(this::onQuantityEditCommit); // Link to the handler method


        // Configure columns with centered alignment
        productNameColumn.setCellFactory(createCenteredCellFactory());
        productPriceColumn.setCellFactory(createCenteredCellFactory());
        productTotalColumn.setCellFactory(createCenteredCellFactory());

// Update quantity column cell factory
        productQuantityColumn.setCellFactory(_ -> {
            SafeIntegerStringConverter converter = new SafeIntegerStringConverter();

            TextFieldTableCell<SaleItem, Integer> cell = new TextFieldTableCell<>(converter) {
                @Override
                public void startEdit() {
                    // Capture original value when editing starts
                    if (!isEmpty()) {
                        converter.setOriginalValue(getItem());
                    }
                    super.startEdit();
                }

                @Override
                public void updateItem(Integer item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        setText(item.toString());
                        setAlignment(Pos.CENTER);
                    }
                }
            };
            cell.setAlignment(Pos.CENTER);
            return cell;
        });

        // Set the items of the table
        productsTable.setItems(saleItemList);

        // Configure actions column
        actionsColumn.setCellFactory(_ -> createActionButtonCell());

        productsTable.setEditable(true); // Enable editing for the table


        // In initialize() method:
        productsTable.sceneProperty().addListener((_, _, newScene) -> {
            if (newScene != null) {
                newScene.addEventFilter(KeyEvent.KEY_PRESSED, this::handleKeyPress);
                newScene.addEventFilter(KeyEvent.KEY_TYPED, this::handleKeyTyped); // Add this line
            }
        });
    }

    private void handleKeyTyped(KeyEvent event) {
        String character = event.getCharacter();

        // Ignore control characters and +/- keys
        if (character.isEmpty() ||
                Character.isISOControl(character.charAt(0)) ||
                character.equals("+") ||
                character.equals("-")) {
            event.consume(); // Prevent text field from processing these characters
            return;
        }

        // Append the character first
        addSaleItemField.appendText(character);

        // Focus the field if not already focused
        if (!addSaleItemField.isFocused()) {
            addSaleItemField.requestFocus();
        }

        // Trigger immediate processing
        addSaleItem();
        event.consume(); // Prevent duplicate character input
    }

    private void handleKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.PLUS || event.getCode() == KeyCode.ADD) {
            handlePlusKey();
            event.consume(); // Prevent key from reaching the text field
        } else if (event.getCode() == KeyCode.MINUS || event.getCode() == KeyCode.SUBTRACT) {
            handleMinusKey();
            event.consume(); // Prevent key from reaching the text field
        }
    }

    private void handlePlusKey() {
        if (!saleItemList.isEmpty()) {
            SaleItem lastItem = saleItemList.getLast();
            lastItem.increaseQuantity(1); // Increase quantity by 1
            refreshData();
        }
    }

    private void handleMinusKey() {
        if (!saleItemList.isEmpty()) {
            SaleItem lastItem = saleItemList.getLast();
            int newQuantity = lastItem.getQuantity() - 1;

            if (newQuantity <= 0) {
                // Remove the product if quantity becomes zero
                saleItemList.remove(lastItem);
            } else {
                // Update the quantity
                lastItem.setQuantity(newQuantity);
            }

            refreshData();
        }
    }

    // Helper method to create centered cell factories
    private <T> Callback<TableColumn<SaleItem, T>, TableCell<SaleItem, T>> createCenteredCellFactory() {
        return _ -> new TableCell<>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item.toString());
                    setAlignment(Pos.CENTER);
                }
            }
        };
    }



    @FXML
    private void clearSale() {
        saleItemList.clear();
        refreshData();
    }

    @FXML
    private void completeSale() {
        Sale newSale = new Sale(saleItemList);

        salesManager.addSale(newSale);
        clearSale();
    }

    @FXML
    private void addSaleItem() {
        ProductListManager productListManager = ProductListManager.getInstance();
        Product p = productListManager.getProductByBarcode(addSaleItemField.getText());

        if (p != null) {
            Optional<SaleItem> existingSaleItem = saleItemList.stream()
                    .filter(item -> item.getProduct().equals(p))
                    .findFirst();

            if (existingSaleItem.isPresent()) {
                // Increase quantity and move to the end of the list
                SaleItem item = existingSaleItem.get();
                item.increaseQuantity(1);
                saleItemList.remove(item);
                saleItemList.add(item); // Now it's the last item
            } else {
                // Add new item to the end
                saleItemList.add(new SaleItem(p, 1));
            }

            refreshData();
            addSaleItemField.clear();
        }
    }

    private void refreshData() {
        productsTable.refresh(); // Force UI update
        refreshTotal();
    }

    @FXML
    private void onQuantityEditCommit(TableColumn.CellEditEvent<SaleItem, Integer> event) {
        SaleItem saleItem = event.getRowValue();
        Integer newQuantity = event.getNewValue();
        Integer oldQuantity = event.getOldValue();

        // Only update if value changed and valid
        if (newQuantity != null && newQuantity > 0 && !newQuantity.equals(oldQuantity)) {
            saleItem.setQuantity(newQuantity);
            refreshData();
        }
    }

    private void refreshTotal(){
        totalPriceLabel.setText(getTotalAmount() + " DA");
    }

    private double getTotalAmount() {
        double total = 0;
        for(SaleItem saleItem: saleItemList){
            total += saleItem.getTotalAmount().doubleValue();
        }

        return total;
    }


    // Helper method to create StringProperty cell value factory
    private Callback<TableColumn.CellDataFeatures<SaleItem, String>, ObservableValue<String>> createStringPropertyCellValueFactory(Function<SaleItem, String> propertyExtractor) {
        return cellData -> new SimpleStringProperty(propertyExtractor.apply(cellData.getValue()));
    }

    // Helper method to create IntegerProperty cell value factory
    private Callback<TableColumn.CellDataFeatures<SaleItem, Integer>, ObservableValue<Integer>> createIntegerPropertyCellValueFactory(Function<SaleItem, Integer> propertyExtractor) {
        return cellData -> new SimpleIntegerProperty(propertyExtractor.apply(cellData.getValue())).asObject();
    }

    // Helper method to create DoubleProperty cell value factory
    private Callback<TableColumn.CellDataFeatures<SaleItem, Double>, ObservableValue<Double>> createDoublePropertyCellValueFactory(Function<SaleItem, Double> propertyExtractor) {
        return cellData -> new SimpleDoubleProperty(propertyExtractor.apply(cellData.getValue())).asObject();
    }

    private TableCell<SaleItem, Void> createActionButtonCell() {
        TableCell<SaleItem, Void> tableCell = new TableCell<>() {
            private final Button removeButton = new Button("Remove");

            {
                // Remove all padding from the cell itself
                setPadding(new Insets(0));
                setStyle("-fx-padding: 0; -fx-background-insets: 0;");

                // Make button fill entire cell
                removeButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                removeButton.prefWidthProperty().bind(widthProperty());

                // Keep text visible by removing ContentDisplay.GRAPHIC_ONLY
                removeButton.setStyle("-fx-background-radius: 10;");
                removeButton.setAlignment(Pos.CENTER);  // Keep text centered

                removeButton.setOnAction(_ -> {
                    SaleItem saleItem = getTableRow().getItem();
                    if (saleItem != null) {
                        saleItemList.remove(saleItem);
                    }
                });
            }

            @Override
            public void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(removeButton);
                }
            }
        };

        tableCell.setStyle("-fx-padding: 0; -fx-background-insets: 0;");
        return tableCell;
    }
}
