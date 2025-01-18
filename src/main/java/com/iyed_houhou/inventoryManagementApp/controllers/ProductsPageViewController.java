package com.iyed_houhou.inventoryManagementApp.controllers;

import com.iyed_houhou.inventoryManagementApp.customFxmlNodes.ProductCard;
import com.iyed_houhou.inventoryManagementApp.managers.ProductListManager;
import com.iyed_houhou.inventoryManagementApp.managers.SupplierListManager;
import com.iyed_houhou.inventoryManagementApp.models.Product;
import com.iyed_houhou.inventoryManagementApp.models.Supplier;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.util.StringConverter;

import java.util.List;

public class ProductsPageViewController extends BasePageController {

    private final ProductListManager productListManager = ProductListManager.getInstance();
    private final SupplierListManager supplierListManager = SupplierListManager.getInstance();

    @FXML
    private ComboBox<Supplier> supplierComboBox;

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

    @FXML
    private void initialize() {
        // Assuming productListManager is properly initialized, populate the product list from the manager
        List<Product> initialProductList = productListManager.getProductList();
        for (Product p : initialProductList) {
            addProductToContainer(p);
        }

        // Fetch the suppliers from the database (via SupplierService)
        List<Supplier> suppliers = supplierListManager.getSupplierList();

        if (!suppliers.isEmpty()) {
            // Populate the ComboBox with Supplier objects
            supplierComboBox.getItems().addAll(suppliers);

            // Set a custom string converter for the ComboBox to show only the Supplier name
            supplierComboBox.setConverter(new StringConverter<>() {
                @Override
                public String toString(Supplier supplier) {
                    return supplier != null ? supplier.getName() : "";
                }

                @Override
                public Supplier fromString(String string) {
                    // Not needed in this context
                    return null;
                }
            });

            // Set the first supplier as the default selected item
            supplierComboBox.getSelectionModel().selectFirst();
        }

        // Handle the selection of a supplier from the ComboBox
        supplierComboBox.setOnAction(event -> {
            Supplier selectedSupplier = supplierComboBox.getSelectionModel().getSelectedItem();
            if (selectedSupplier != null) {
                System.out.println("Selected: " + selectedSupplier.getName());
            }
        });
    }


    private void addProductToContainer(Product product) {
        ProductCard newProductCard = new ProductCard(product);
        productContainer.getChildren().add(newProductCard);
    }

    // This will handle the "Add Product" button click
    @FXML
    private void addProduct() {
        try {
            Product newProduct = getNewProductFromInputFields();


            // Check if the product exists in the list
            if(newProduct == null) {
                return;
            }
            Product existingProduct = productListManager.searchProductByBarcode(newProduct.getProductBarCode());


            if (existingProduct == null) {
                // Product doesn't exist, so we add it
                productListManager.addProduct(newProduct);
                addProductToContainer(newProduct);
            } else {
                // Product exists, update it
                existingProduct.addQuantity(newProduct);
                productListManager.updateProduct(existingProduct); // Update the product in the manager
                ProductCard existingProductCard = searchForProduct(existingProduct.getProductBarCode());
                if (existingProductCard != null) {
                    existingProductCard.SetProduct(existingProduct);
                    existingProductCard.refreshUI();
                }
            }

            // Reset fields after adding or updating the product
            clearFields();
        } catch (Exception e) {
            throw new RuntimeException("Failed to add or update the product.", e);
        }
    }

    private Product getNewProductFromInputFields() {

        // Validate and retrieve product ID
        String barCode = productIdField.getText();
        if (barCode == null || barCode.trim().isEmpty()) {
            // Show an alert to the user
            showAlert("Missing Input", "Product ID cannot be empty.", Alert.AlertType.ERROR);
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
                if (productQuantity < 0) {
                    showAlert("Invalid Input", "Product quantity must be a positive value.", Alert.AlertType.ERROR);
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

        // Retrieve selected supplier from the ComboBox
        Supplier selectedSupplier = supplierComboBox.getSelectionModel().getSelectedItem();
        if (selectedSupplier == null && !supplierComboBox.getItems().isEmpty()) {
            selectedSupplier = supplierComboBox.getItems().getFirst(); // Default to the first supplier
        }


        // Create and return the new Product object
        return new Product(barCode, productName, productPrice, productQuantity, buyPrice, selectedSupplier);
    }

    private ProductCard searchForProduct(String newProductId) {
        for (Node n : productContainer.getChildren()) {
            if (n instanceof ProductCard && ((ProductCard) n).getProduct().getProductBarCode().equals(newProductId)) {
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
}
