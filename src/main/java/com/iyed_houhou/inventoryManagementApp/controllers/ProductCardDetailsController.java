package com.iyed_houhou.inventoryManagementApp.controllers;

import com.iyed_houhou.inventoryManagementApp.models.Product;
import com.iyed_houhou.inventoryManagementApp.services.ProductService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
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
    public void saveProductDetails() {
        // Initialize the input control variables and check for null values
        String productName = getTextFromField(productNameField);
        String productPrice = getTextFromField(productPriceField);
        String buyPrice = getTextFromField(buyPriceField);
        String productQuantity = getTextFromField(productQuantityField);
        String productSupplier = getTextFromField(productSupplierField);
        String productImgURL = getTextFromField(productImageField);

        // Check if the product name is empty
        if (productName.isEmpty()) {
            showError("Product name cannot be empty.");
            return;
        }

        // Check if the sale price is a valid double and greater than zero
        double salePrice;
        try {
            salePrice = Double.parseDouble(productPrice);
            if (salePrice <= 0) {
                showError("Sale price must be a positive number.");
                return;
            }
        } catch (NumberFormatException e) {
            showError("Invalid sale price. Please enter a valid number.");
            return;
        }

        // Check if the buy price is a valid double and greater than zero
        double buyPriceValue;
        try {
            buyPriceValue = Double.parseDouble(buyPrice);
            if (buyPriceValue <= 0) {
                showError("Buy price must be a positive number.");
                return;
            }
        } catch (NumberFormatException e) {
            showError("Invalid buy price. Please enter a valid number.");
            return;
        }

        // Check if the quantity is a valid integer and greater than or equal to zero
        int quantity;
        try {
            quantity = Integer.parseInt(productQuantity);
            if (quantity < 0) {
                showError("Quantity cannot be negative.");
                return;
            }
        } catch (NumberFormatException e) {
            showError("Invalid quantity. Please enter a valid integer.");
            return;
        }

        // Check if the supplier name is empty
        if (productSupplier.isEmpty()) {
            showError("Supplier name cannot be empty.");
            return;
        }


        // Set the validated values to the product object
        product.setName(productName);
        product.setSalePrice(salePrice);
        product.setBuyPrice(buyPriceValue);
        product.setQuantity(quantity);
        product.getSupplier().setName(productSupplier);  // Assuming Supplier has a setter for name
        product.setImgURL(productImgURL);

        // Save the product using ProductService
        ProductService productService = new ProductService();
        productService.updateProduct(product);
    }

    // Helper method to handle null checks on TextField input
    private String getTextFromField(TextField textField) {
        return (textField != null && textField.getText() != null) ? textField.getText().trim() : "";
    }

    private void showError(String message) {
        // Show error message (could be a pop-up, label, etc.)
        // For example, use an alert dialog:
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Input Error");
        alert.setHeaderText("Invalid input");
        alert.setContentText(message);
        alert.showAndWait();
    }



}
