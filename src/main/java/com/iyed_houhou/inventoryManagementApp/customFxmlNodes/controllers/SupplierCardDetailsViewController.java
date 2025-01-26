package com.iyed_houhou.inventoryManagementApp.customFxmlNodes.controllers;

import com.iyed_houhou.inventoryManagementApp.managers.SupplierListManager;
import com.iyed_houhou.inventoryManagementApp.models.Supplier;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class SupplierCardDetailsViewController {
    @FXML
    private TextArea supplierOtherField;
    @FXML
    private TextField supplierNameField;
    @FXML
    private TextField supplierContactField;

    private Supplier supplier;

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
        supplierNameField.setText(supplier.getName());
        supplierContactField.setText(supplier.getContact());
        supplierOtherField.setText(supplier.getDescription());
    }

    public void saveSupplierDetails() {
        String name = supplierNameField.getText();
        if(name.trim().isEmpty()){
            name = supplier.getName();
        }
        String contact = supplierContactField.getText();
        if(contact.trim().isEmpty()){
            contact = supplier.getContact();
        }
        String description = supplierOtherField.getText();

        supplier.setName(name);
        supplier.setContact(contact);
        supplier.setDescription(description);

        System.out.println(supplier.getDescription());

        SupplierListManager supplierListManager = SupplierListManager.getInstance();
        supplierListManager.updateSupplier(supplier);
    }
}
