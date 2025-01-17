package com.iyed_houhou.inventoryManagementApp.models;

import java.util.Date;

public class Transaction {
    private String transactionId;
    private Date date;
    private User user;
    private Sale sale;
    private double totalAmount;

    public Transaction(String transactionId, Date date, User user, Sale sale, double totalAmount) {
        this.transactionId = transactionId;
        this.date = date;
        this.user = user;
        this.sale = sale;
        this.totalAmount = totalAmount;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public Date getDate() {
        return date;
    }

    public User getUser() {
        return user;
    }

    public Sale getSale() {
        return sale;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    // Setters and additional methods can be added as necessary
}

