package com.iyed_houhou.inventoryManagementApp.controllers;

import com.iyed_houhou.inventoryManagementApp.models.Product;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ProductCardDetailsController {

    @FXML
    private TextField productNameField;
    @FXML
    private TextField productPriceField;
    @FXML
    private TextField buyPriceField;
    @FXML
    private TextField avgBuyPriceField;
    @FXML
    private TextField productQuantityField;
    @FXML
    private TextField productSupplierField;
    @FXML
    private TextField productImageField;
    @FXML
    private Button saveButton;

    private Product product;

    // Method to set the product from the ProductCard
    public void setProduct(Product product) {
        this.product = product;

        // Set the fields with current product data
        productNameField.setText(product.getName());
        productPriceField.setText(String.valueOf(product.getSalePrice()));
        buyPriceField.setText(String.valueOf(product.getBuyPrice()));
        avgBuyPriceField.setText(String.valueOf(product.getAvgBuyPrice()));
        productQuantityField.setText(String.valueOf(product.getQuantity()));
        productSupplierField.setText(product.getSupplier().getName());
        productImageField.setText(product.getImgURL());
    }

    @FXML
    private void saveProductDetails() {
        // Save the updated details from the fields into the product object
        product.setName(productNameField.getText());
        product.setSalePrice(Double.parseDouble(productPriceField.getText()));
        product.setBuyPrice(Double.parseDouble(buyPriceField.getText()));
        product.setQuantity(Integer.parseInt(productQuantityField.getText()));
        product.getSupplier().setName(productSupplierField.getText());  // Assuming the Supplier object has a setter
        product.setImgURL(productImageField.getText());
    }

}
