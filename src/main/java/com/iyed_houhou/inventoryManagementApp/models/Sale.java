package com.iyed_houhou.inventoryManagementApp.models;


import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class Sale {
    private int saleId;
    private Date saleDate;
    private BigDecimal totalAmount;
    private List<SaleItem> saleItems ;

    public Sale(int saleId , List<SaleItem> saleItems){
        this.saleId = saleId;
        this.saleDate = new Date();
        this.saleItems = saleItems;
        totalAmount = calculateTotalAmount();
    }

    public Sale( List<SaleItem> saleItems){
        this.saleDate = new Date();
        this.saleItems = saleItems;
        totalAmount = calculateTotalAmount();
    }

    public Sale( int saleId, Date date, List<SaleItem> saleItems){
        this.saleDate = new Date();
        this.saleItems = saleItems;
        totalAmount = calculateTotalAmount();
    }

    public Sale(int saleId, Timestamp saleDate, BigDecimal totalAmount, List<SaleItem> items) {
        this.saleId = saleId;
        this.saleDate = new Date(saleDate.getTime());
        this.totalAmount = totalAmount;
        this.saleItems = items;
    }

    public void setSaleItemId() {
        for (SaleItem saleItem: saleItems){
            saleItem.setSaleId(saleId);
        }
    }

    private BigDecimal calculateTotalAmount() {
        BigDecimal totalAmount = new BigDecimal(0);
        for (SaleItem saleItem : saleItems){
            totalAmount = totalAmount.add(saleItem.getTotalAmount());
        }
        return totalAmount;
    }

    public int getSaleId() {
        return saleId;
    }

    public void setSaleId(int saleId) {
        this.saleId = saleId;
    }

    public Date getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(Date saleDate) {
        this.saleDate = saleDate;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<SaleItem> getSaleItems() {
        return saleItems;
    }

    public void setSaleItems(List<SaleItem> saleItems) {
        this.saleItems = saleItems;
    }

}
