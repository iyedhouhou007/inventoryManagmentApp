package com.iyed_houhou.inventoryManagementApp.models;

import java.util.ArrayList;
import java.util.List;

public class Supplier {
    private int supplierId;  // ID for identification in the DB (if needed)
    private String name;
    private String contact;
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
        if (contact != null && contact.matches("^[0-9]{10}$")) {  // Example validation
            this.contact = contact;
        } else {
            throw new IllegalArgumentException("Invalid contact number");
        }
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
