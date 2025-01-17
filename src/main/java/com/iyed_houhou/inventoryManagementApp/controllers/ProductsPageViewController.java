package com.iyed_houhou.inventoryManagementApp.controllers;

import com.iyed_houhou.inventoryManagementApp.customFxmlNodes.ProductCard;
import com.iyed_houhou.inventoryManagementApp.models.Product;
import com.iyed_houhou.inventoryManagementApp.models.Supplier;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;

public class ProductsPageViewController extends BasePageController {


    @FXML
    private void openDashboardPage() {
        loadPage("DashboardView.fxml");
    }

    @FXML
    private void openProductsPage() {
        loadPage("ProductsPageView.fxml");
    }

    @FXML
    private void openReportsPage() {
        loadPage("ReportsPageView.fxml");
    }

    @FXML
    private void openSettingsPage() {
        loadPage("SettingsPageView.fxml");
    }

    @FXML
    private void openAddSalePage() {
        loadPage("AddSalePageView.fxml");
    }

    @FXML
    private void openSuppliersPage() {
        loadPage("SuppliersPageView.fxml");
    }


    @FXML
    private FlowPane productContainer;  // This is where the ProductCards will be displayed

    @FXML
    private TextField productIdField;

    @FXML
    private TextField productNameField;

    @FXML
    private TextField productPriceField;

    @FXML
    private TextField productQuantityField;

    @FXML
    private TextField productBuyPriceField;


    // This will handle the "Add Product" button click
    @FXML
    private void addProduct() {
        try{
            Product newProduct = getNewProductFromInputFields();

            assert newProduct != null;
            ProductCard lastProductCard = searchForProduct(newProduct.getProductId());

            if( lastProductCard == null){
                lastProductCard = new ProductCard(newProduct);
                productContainer.getChildren().add(lastProductCard);
            }else {
                lastProductCard.getProduct().addQuantity(newProduct);
                lastProductCard.refreshUI();
            }


            // Reset fields after adding
            clearFields();

        } catch (Exception e) {

            throw new RuntimeException(e);
        }
    }

    private Product getNewProductFromInputFields() {

        // Validate and retrieve product ID
        String productIdText = productIdField.getText();
        if (productIdText == null || productIdText.trim().isEmpty()) {
            // Show an alert to the user
            showAlert("Missing Input", "Product ID cannot be empty.", Alert.AlertType.ERROR);
            return null; // Exit the method gracefully
        }

        int productId;
        try {
            productId = Integer.parseInt(productIdText); // Parse to integer
        } catch (NumberFormatException e) {
            // Handle invalid number format
            showAlert("Invalid Input", "Product ID must be a numeric value.", Alert.AlertType.ERROR);
            return null; // Exit the method gracefully
        }

        String productName = productNameField.getText();
        // Retrieve and validate product price
        double productPrice = 0; // Default value
        String productPriceText = productPriceField.getText();
        if (productPriceText != null && !productPriceText.trim().isEmpty()) {
            try {
                productPrice = Double.parseDouble(productPriceText);
            } catch (NumberFormatException e) {
                showAlert("Invalid Input", "Product price must be a numeric value.", Alert.AlertType.ERROR);
                return null; // Exit method gracefully if invalid
            }
        }

        // Retrieve and validate product quantity
        int productQuantity = 0; // Default value
        String productQuantityText = productQuantityField.getText();
        if (productQuantityText != null && !productQuantityText.trim().isEmpty()) {
            try {
                productQuantity = Integer.parseInt(productQuantityText);
                if(productQuantity < 0) {
                    showAlert("Invalid Input", "Product price must be a positive value.", Alert.AlertType.ERROR);
                    return null;
                }
            } catch (NumberFormatException e) {
                showAlert("Invalid Input", "Product quantity must be a numeric value.", Alert.AlertType.ERROR);
                return null; // Exit method gracefully if invalid
            }
        }

        // Retrieve and validate buy price
        int buyPrice = 0; // Default value
        String buyPriceText = productBuyPriceField.getText();
        if (buyPriceText != null && !buyPriceText.trim().isEmpty()) {
            try {
                buyPrice = Integer.parseInt(buyPriceText);
            } catch (NumberFormatException e) {
                showAlert("Invalid Input", "Buy price must be a numeric value.", Alert.AlertType.ERROR);
                return null; // Exit method gracefully if invalid
            }
        }


        return new Product(productId,productName,productPrice, productQuantity, buyPrice, new Supplier("iyed", "contact"));
    }


    private ProductCard searchForProduct(int newProductId){

        for(Node n : productContainer.getChildren()){
            if(n instanceof ProductCard && ((ProductCard) n).getProduct().getProductId() == newProductId){
                System.out.println("foundProduct");
                return (ProductCard) n;
            }
        }

        return null;
    }
    // This method resets all fields after a product is added
    private void clearFields() {
        productIdField.clear();
        productNameField.clear();
        productPriceField.clear();
        productQuantityField.clear();
        productBuyPriceField.clear();
    }
}

