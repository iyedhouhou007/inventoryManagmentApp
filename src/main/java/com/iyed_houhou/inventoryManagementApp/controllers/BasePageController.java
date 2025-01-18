package com.iyed_houhou.inventoryManagementApp.controllers;

import com.iyed_houhou.inventoryManagementApp.Main;
import com.iyed_houhou.inventoryManagementApp.config.AppConfig;
import com.iyed_houhou.inventoryManagementApp.utils.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BasePageController {

    // Logger instance
    private static final Logger logger = Logger.getLogger(BasePageController.class.getName());

    // Common method for logging out (protected access)
    @FXML
    protected void logout() {
        logger.info("Attempting to log out...");
        try {
            // Clear session using SessionManager
            SessionManager.getInstance().clearSession();
            logger.info("Session cleared successfully.");

            // Redirect to login page
            loadPage("LoginView.fxml");

            // Optionally, show an alert confirming the logout
            showAlert("Info", "You have been logged out.", Alert.AlertType.CONFIRMATION);

            logger.info("User successfully logged out and redirected to LoginView.");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "An error occurred during logout.", e);
        }
    }

    protected void loadPage(String pageName) {
        logger.info("Attempting to load page: " + pageName);
        try {
            String PAGE_FOLDER_PATH = AppConfig.VIEW_PATH;
            String pagePath = PAGE_FOLDER_PATH + pageName;

            // Load the new FXML content
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(pagePath));
            javafx.scene.Parent newRoot = fxmlLoader.load();

            Stage currentStage = AppConfig.OWNER;
            if (currentStage == null) {
                throw new IllegalStateException("Stage is not initialized.");
            }

            // Get the current scene
            Scene currentScene = currentStage.getScene();
            if (currentScene == null) {
                throw new IllegalStateException("Scene is not initialized.");
            }

            // Update the root of the current scene instead of creating a new scene
            currentScene.setRoot(newRoot);

            logger.info("Page loaded successfully: " + pageName);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to load the page: " + pageName, e);

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to Load");
            alert.setContentText("Unable to load the requested page. Please contact support.");
            alert.showAndWait();
        }
    }

    protected void showAlert(String title, String message, Alert.AlertType type) {
        logger.info("Showing alert with title: " + title);
        try {
            // Make sure AppConfig.OWNER is not null and is initialized
            if (AppConfig.OWNER == null) {
                logger.warning("Main window (AppConfig.OWNER) is not set.");
                return;  // Avoid showing alert if the main window is not set
            }

            Alert alert = new Alert(type);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);

            // Apply CSS styles to the alert
            alert.getDialogPane().getStylesheets().add(Objects.requireNonNull(getClass().getResource(AppConfig.STYLES_PATH + "alertStyles.css")).toExternalForm());

            // Set the owner of the alert
            alert.initOwner(AppConfig.OWNER);

            // Ensure the main window is brought to the front before the alert
            AppConfig.OWNER.toFront();

            // Make the alert modal
            alert.initModality(Modality.APPLICATION_MODAL);

            // Show the alert and wait for user interaction
            alert.showAndWait();
            logger.info("Alert displayed successfully.");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to show alert with title: " + title, e);
        }
    }

}
