package com.iyed_houhou.inventoryManagementApp.controllers;

import com.iyed_houhou.inventoryManagementApp.models.Product;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ProductCardDetailsController {

    @FXML
    private Label productNameLabel;
    @FXML
    private Label productPriceLabel;
    @FXML
    private Label productSupplierLabel;
    @FXML
    private Label productQuantityLabel;

    public void setProduct(Product product) {
        productNameLabel.setText(product.getName());
        productPriceLabel.setText("DA" + product.getSalePrice());
        productSupplierLabel.setText(product.getSupplier().getName());
        productQuantityLabel.setText("Quantity: " + product.getQuantity());
    }
}
