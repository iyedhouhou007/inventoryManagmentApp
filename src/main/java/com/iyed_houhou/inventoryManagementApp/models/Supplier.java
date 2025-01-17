package com.iyed_houhou.inventoryManagementApp.models;

import java.util.ArrayList;
import java.util.List;

public class Supplier {
    private String supplierId;
    private String name;
    private String contact;
    private List<Product> productsSupplied;

    public Supplier(String name, String contact) {
        this.name = name;
        this.contact = contact;
        this.productsSupplied = new ArrayList<>();
    }

    public String getSupplierId() {
        return supplierId;
    }

    public String getName() {
        return name;
    }

    public String getContact() {
        return contact;
    }

    public List<Product> getProductsSupplied() {
        return productsSupplied;
    }

    public void addProduct(Product product) {
        this.productsSupplied.add(product);
    }

    // Setters and additional methods can be added as necessary
}

