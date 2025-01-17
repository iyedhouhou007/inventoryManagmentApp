package com.iyed_houhou.inventoryManagementApp.models;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Report {
    private Date startDate;
    private Date endDate;
    private BigDecimal totalRevenue;
    private BigDecimal totalCost;
    private BigDecimal profit;
    private List<Sale> sales;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public Report(Date startDate, Date endDate, List<Sale> sales) {
        if (startDate.after(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }

        this.startDate = startDate;
        this.endDate = endDate;
        this.sales = sales;
        this.totalRevenue = BigDecimal.ZERO;
        this.totalCost = BigDecimal.ZERO;
        this.profit = BigDecimal.ZERO;
        calculateReport();
    }

    private void calculateReport() {
        for (Sale sale : sales) {
            if (isSaleInRange(sale)) {
                totalRevenue = totalRevenue.add(sale.getTotalAmount());
                totalCost = totalCost.add(BigDecimal.valueOf(sale.getProduct().getSalePrice()).multiply(BigDecimal.valueOf(sale.getQuantity())));
            }
        }
        profit = totalRevenue.subtract(totalCost);
    }

    private boolean isSaleInRange(Sale sale) {
        if (sale.getSaleDate() == null) {
            return false; // or log an error
        }
        Date normalizedStartDate = normalizeDate(startDate);
        Date normalizedEndDate = normalizeDate(endDate);
        Date saleDateNormalized = normalizeDate(sale.getSaleDate());

        return saleDateNormalized.after(normalizedStartDate) && saleDateNormalized.before(normalizedEndDate);
    }

    private Date normalizeDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public void generateSalesReport() {
        System.out.println("Sales Report from " + dateFormat.format(startDate) + " to " + dateFormat.format(endDate));
        System.out.println("Total Revenue: " + totalRevenue);
        System.out.println("Total Cost: " + totalCost);
        System.out.println("Profit: " + profit);
        System.out.println("\nSales Breakdown:");

        for (Sale sale : sales) {
            if (isSaleInRange(sale)) {
                System.out.println(sale.getProduct().getName() + ": " + sale.getQuantity() + " units sold at " + sale.getTotalAmount());
            }
        }
    }

    public void generateInventoryReport() {
        System.out.println("Inventory Report from " + dateFormat.format(startDate) + " to " + dateFormat.format(endDate));
        for (Sale sale : sales) {
            if (isSaleInRange(sale)) {
                System.out.println(sale.getProduct().getName() + ": " + sale.getQuantity() + " units sold.");
            }
        }
    }

    // Getters and setters for attributes
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public BigDecimal getProfit() {
        return profit;
    }

    public List<Sale> getSales() {
        return sales;
    }

    public void setSales(List<Sale> sales) {
        this.sales = sales;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public void setProfit(BigDecimal profit) {
        this.profit = profit;
    }

    @Override
    public String toString() {
        return "Sales Report [" +
                "From: " + dateFormat.format(startDate) +
                " To: " + dateFormat.format(endDate) +
                ", Total Revenue: " + totalRevenue +
                ", Total Cost: " + totalCost +
                ", Profit: " + profit +
                "]";
    }
}
