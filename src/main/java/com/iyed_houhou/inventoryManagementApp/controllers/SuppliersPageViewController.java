package com.iyed_houhou.inventoryManagementApp.controllers;


import com.iyed_houhou.inventoryManagementApp.customFxmlNodes.SupplierCard;
import com.iyed_houhou.inventoryManagementApp.managers.SupplierListManager;
import com.iyed_houhou.inventoryManagementApp.models.Role;
import com.iyed_houhou.inventoryManagementApp.models.Supplier;
import com.iyed_houhou.inventoryManagementApp.models.User;
import com.iyed_houhou.inventoryManagementApp.utils.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;

import java.util.List;

public class SuppliersPageViewController extends BasePageController{

    private final SupplierListManager supplierListManager = SupplierListManager.getInstance();
    // FXML Fields
    @FXML
    private TextField supplierNameField;
    @FXML
    private TextField supplierContactField;
    @FXML
    private TextField supplierEmailField;
    @FXML
    private FlowPane suppliersContainer;

    // List to hold supplier names (In real case, this would come from a database)
    private final List<Supplier> suppliersList = supplierListManager.getSupplierList();

    // This method is called when the view is loaded
    @FXML
    public void initialize() {
        User loggedInUser = SessionManager.getInstance().getLoggedInUser();
        if (loggedInUser == null || (!loggedInUser.getRole().equals(Role.Admin) && !loggedInUser.getRole().equals(Role.SuperAdmin)) ) {
            adminOnlyPage.setVisible(false);
        }
        // Populate the suppliers list
        populateSuppliersList();
    }

    // This method will populate the suppliers list dynamically
    private void populateSuppliersList() {
        suppliersContainer.getChildren().clear(); // Clear existing items
        for (Supplier supplier : suppliersList) {
            if (!supplier.getName().equals("default")){
                SupplierCard supplierCard = new SupplierCard(supplier);
                suppliersContainer.getChildren().add(supplierCard);
            }
        }
    }

    // This method will handle adding a new supplier
    @FXML
    private void addSupplier() {
        String name = supplierNameField.getText();
        String contact = supplierContactField.getText();
        String email = supplierEmailField.getText();

        // Simple validation check
        if (name.isEmpty() || contact.isEmpty() || email.isEmpty()) {
            showAlert( "Input Error", "Please fill in all fields.", Alert.AlertType.WARNING);
            return;
        }

        Supplier supplier = new Supplier(name,  contact , email);

        // Add supplier to list (in real app, this would be saved to a database)
        supplierListManager.addSupplier(supplier);

        // Clear fields
        supplierNameField.clear();
        supplierContactField.clear();
        supplierEmailField.clear();

        // Refresh the list
        populateSuppliersList();
    }



}
