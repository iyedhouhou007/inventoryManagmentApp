package com.iyed_houhou.inventoryManagementApp.models;

import java.math.BigDecimal;
import java.util.Date;

public class SaleItem {
    private int saleId;
    private Date saleDate;
    private Product product;
    private int quantity;
    private BigDecimal totalAmount;

    public SaleItem(int saleId, Date saleDate, Product product, int quantity) {
        this.saleId = saleId;
        this.saleDate = saleDate;
        this.product = product;
        this.quantity = quantity;
        this.totalAmount = BigDecimal.valueOf(product.getSalePrice()*quantity);
    }

    public SaleItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
        this.totalAmount = BigDecimal.valueOf(product.getSalePrice()*quantity);
    }

    public int getSaleId() {
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

    public void increaseQuantity(int addedQuantity) {
        quantity += addedQuantity;
        totalAmount = BigDecimal.valueOf(product.getSalePrice()*quantity);
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }


    public void setSaleId(int saleId) {
        this.saleId = saleId;
    }

    public void setSaleDate(Date saleDate) {
        this.saleDate = saleDate;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setQuantity(int quantity) {
        totalAmount = BigDecimal.valueOf(product.getSalePrice()*quantity);
        this.quantity = quantity;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }



}
