package com.iyed_houhou.inventoryManagementApp.customFxmlNodes;

import com.iyed_houhou.inventoryManagementApp.config.AppConfig;
import com.iyed_houhou.inventoryManagementApp.controllers.SupplierCardDetailsViewController;
import com.iyed_houhou.inventoryManagementApp.managers.ProductListManager;
import com.iyed_houhou.inventoryManagementApp.managers.SupplierListManager;
import com.iyed_houhou.inventoryManagementApp.models.Supplier;
import com.iyed_houhou.inventoryManagementApp.utils.UIUtils;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SupplierCard extends VBox {
    private Supplier supplier;
    private final SupplierListManager supplierListManager;
    private static final Logger logger = Logger.getLogger(SupplierCard.class.getName());

    public SupplierCard(Supplier supplier) {
        supplierListManager = SupplierListManager.getInstance();
        this.supplier = supplier;

        // Set the alignment and spacing for the VBox
        this.setAlignment(Pos.CENTER);
        this.setSpacing(10);
        this.setMaxWidth(350);
        this.setMinWidth(150);
        this.getStyleClass().add("supplier-card");

        // Add supplier name
        Label supplierName = new Label(supplier.getName());
        supplierName.setFont(Font.font(18));
        supplierName.setTextFill(Color.BLACK);
        supplierName.setWrapText(true); // Allow text to wrap
        supplierName.setMaxWidth(300); // Set max width for the label
        supplierName.getStyleClass().add("supplier-name");

        // Add supplier contact info
        Label supplierContact = new Label("Contact: " + supplier.getContact());
        supplierContact.setFont(Font.font(16));
        supplierContact.setTextFill(Color.DARKGRAY);
        supplierContact.setWrapText(true); // Allow text to wrap
        supplierContact.setMaxWidth(300);

        Label supplierDescription = new Label("Description: " + supplier.getDescription());
        supplierDescription.setFont(Font.font(16));
        supplierDescription.setTextFill(Color.BLACK);
        supplierDescription.setWrapText(true); // Allow text to wrap
        supplierDescription.setMaxWidth(300);

        // Add a remove supplier button
        Button removeButton = new Button("Remove Supplier");
        removeButton.getStyleClass().add("remove-supplier-btn");
        removeButton.setOnAction(event -> handleRemoveSupplier());

        // Add all elements back to the VBox
        this.getChildren().addAll(supplierName, supplierContact, supplierDescription, removeButton);
        // Add click event handler
        this.setOnMouseClicked(this::handleClick);

    }

    private void handleClick(MouseEvent event) {
        System.out.println("Supplier clicked: " + supplier.getName());
        showSupplierDetails();
    }

    // Method to handle supplier removal
    private void handleRemoveSupplier() {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Remove Supplier");
        confirmationAlert.setHeaderText("Are you sure you want to remove this supplier?");
        confirmationAlert.setContentText("Supplier: " + supplier.getName());

        // Set the owner of the alert
        confirmationAlert.initOwner(AppConfig.MAIN_PRIMARY_STAGE);

        // Ensure the main window is brought to the front before the alert
        AppConfig.MAIN_PRIMARY_STAGE.toFront();

        // Make the alert modal
        confirmationAlert.initModality(Modality.APPLICATION_MODAL);
        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            System.out.println("Supplier removed: " + supplier.getName());
            // Add logic to remove the supplier from the database or list
            removeFromUI();
        }
    }

    // Method to remove the supplier card from the UI
    private void removeFromUI() {
        // Assuming this SupplierCard is part of a container (e.g., a VBox or GridPane),
        // get the parent and remove this SupplierCard from it.
        if (this.getParent() instanceof javafx.scene.layout.Pane parent) {
            supplierListManager.removeSupplier(supplier);
            parent.getChildren().remove(this);
            ProductListManager.getInstance().refreshProductsList();
        }
    }

    private void showSupplierDetails() {
        try {
            // Load the FXML file containing the DialogPane
            FXMLLoader loader = new FXMLLoader(getClass().getResource(AppConfig.CUSTOM_DIALOG_PATH + "SupplierDetailsView.fxml"));
            DialogPane dialogPane = loader.load();

            // Optionally, pass the supplier data to the controller
            SupplierCardDetailsViewController controller = loader.getController();
            controller.setSupplier(supplier);

            // Create a Dialog and set the DialogPane
            Dialog<Void> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Supplier Details");

            // Set the dialog's owner to the application's main stage
            dialog.initOwner(AppConfig.MAIN_PRIMARY_STAGE);
            dialog.initModality(Modality.APPLICATION_MODAL);

            // Find the Close button and apply the custom CSS class
            Button closeButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.CLOSE);
            if (closeButton != null) {
                closeButton.getStyleClass().add("close-btn");
            }

            Button applyButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.APPLY);
            if (applyButton != null) {
                applyButton.getStyleClass().add("save-button");
            }

            // Show the dialog and wait for user interaction
            Optional<Void> result = dialog.showAndWait();

            // Only save the details if the "Apply" button was pressed
            if (result.toString().contains("Apply")) {
                System.out.println(true);
                controller.saveSupplierDetails();
                refreshUI();
            }

        } catch (IOException e) {
            // Log the error with a proper message and stack trace
            logger.log(Level.SEVERE, "Failed to load the SupplierCardDetailsViewController.fxml file", e);
        }
    }

    public void refreshUI() {
        // Clear all children to reset the UI
        this.getChildren().clear();

        // Add supplier name
        Label supplierName = new Label(supplier.getName());
        supplierName.setFont(Font.font(18));
        supplierName.setTextFill(Color.BLACK);
        supplierName.setWrapText(true); // Allow text to wrap
        supplierName.setMaxWidth(300);
        supplierName.getStyleClass().add("supplier-name");

        // Add supplier contact info
        Label supplierContact = new Label("Contact: " + supplier.getContact());
        supplierContact.setFont(Font.font(16));
        supplierContact.setTextFill(Color.DARKGRAY);
        supplierContact.setWrapText(true); // Allow text to wrap
        supplierContact.setMaxWidth(300);

        Label supplierDescription = new Label("Description: " + supplier.getDescription());
        supplierDescription.setFont(Font.font(16));
        supplierDescription.setTextFill(Color.BLACK);
        supplierDescription.setWrapText(true); // Allow text to wrap
        supplierDescription.setMaxWidth(300);

        // Add a remove supplier button
        Button removeButton = new Button("Remove Supplier");
        removeButton.getStyleClass().add("remove-supplier-btn");
        removeButton.setOnAction(event -> handleRemoveSupplier());

        // Add all elements back to the VBox
        this.getChildren().addAll(supplierName, supplierContact, supplierDescription, removeButton);
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public Supplier getSupplier() {
        return supplier;
    }
}
