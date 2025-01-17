package com.iyed_houhou.inventoryManagementApp.models;

import java.math.BigDecimal;
import java.util.Date;

public class Sale {
    private String saleId;
    private Date saleDate;
    private Product product;
    private int quantity;
    private BigDecimal totalAmount;

    public Sale(String saleId, Date saleDate, Product product, int quantity, BigDecimal totalAmount) {
        this.saleId = saleId;
        this.saleDate = saleDate;
        this.product = product;
        this.quantity = quantity;
        this.totalAmount = totalAmount;
    }

    public String getSaleId() {
        return saleId;
    }

    public Date getSaleDate() {
        return saleDate;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setSaleId(String saleId) {
        this.saleId = saleId;
    }

    public void setSaleDate(Date saleDate) {
        this.saleDate = saleDate;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
}
