package com.iyed_houhou.inventoryManagementApp.models;

import java.util.List;

public class Inventory {
    private List<Product> products;


    public Inventory(List<Product> products) {
        this.products = products;
    }



    public List<Product> getProducts() {
        return products;
    }

}
