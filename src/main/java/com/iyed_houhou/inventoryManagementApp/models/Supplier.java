package com.iyed_houhou.inventoryManagementApp.models;

import java.util.ArrayList;
import java.util.List;

public class Supplier {
    private int supplierId;  // ID for identification in the DB (if needed)
    private String name;
    private String contact;
    private String description;
    private List<Product> productsSupplied;

    public Supplier(String name, String contact) {
        this.name = name;
        this.contact = contact;
        this.productsSupplied = new ArrayList<>();
    }

    public Supplier(int supplierId, String name, String contact) {
        this.supplierId = supplierId;
        this.name = name;
        this.contact = contact;
        this.productsSupplied = new ArrayList<>();
    }

    public Supplier(int supplierId, String name, String contact , String description) {
        this.supplierId = supplierId;
        this.name = name;
        this.contact = contact;
        this.description = description;
        this.productsSupplied = new ArrayList<>();
    }

    public Supplier(String name, String contact , String description) {
        this.name = name;
        this.contact = contact;
        this.description = description;
        this.productsSupplied = new ArrayList<>();
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public List<Product> getProductsSupplied() {
        return productsSupplied;
    }

    public void addProduct(Product product) {
        this.productsSupplied.add(product);
    }

    public void removeProduct(Product product) {
        this.productsSupplied.remove(product);
    }

    public boolean hasProduct(Product product) {
        return this.productsSupplied.contains(product);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getProductCount() {
        return this.productsSupplied.size();
    }

    @Override
    public String toString() {
        return "Supplier{" +
                "supplierId=" + supplierId +
                ", name='" + name + '\'' +
                ", contact='" + contact + '\'' +
                '}';
    }

}
