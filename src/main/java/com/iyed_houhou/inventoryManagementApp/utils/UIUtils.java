package com.iyed_houhou.inventoryManagementApp.utils;

import com.iyed_houhou.inventoryManagementApp.config.AppConfig;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class UIUtils {

    private static final Logger logger = LoggerFactory.getLogger(UIUtils.class);

    // Static method to show alert
    public static void showAlert(String title, String message, Alert.AlertType type) {
        logger.info("Showing alert with title: {}", title);
        try {
            // Make sure AppConfig.MAIN_PRIMARY_STAGE is not null and is initialized
            if (AppConfig.MAIN_PRIMARY_STAGE == null) {
                logger.warn("Main window (AppConfig.MAIN_PRIMARY_STAGE) is not set.");
                return;  // Avoid showing alert if the main window is not set
            }

            Alert alert = new Alert(type);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);

            // Apply CSS styles to the alert
            alert.getDialogPane().getStylesheets().add(Objects.requireNonNull(UIUtils.class.getResource(AppConfig.STYLES_PATH + "alertStyles.css")).toExternalForm());

            // Set the owner of the alert
            alert.initOwner(AppConfig.MAIN_PRIMARY_STAGE);

            // Ensure the main window is brought to the front before the alert
            AppConfig.MAIN_PRIMARY_STAGE.toFront();

            // Make the alert modal
            alert.initModality(Modality.APPLICATION_MODAL);

            // Show the alert and wait for user interaction
            alert.showAndWait();
            logger.info("Alert displayed successfully.");
        } catch (Exception e) {
            logger.error("Failed to show alert with title: {} {}", e, title);
        }
    }
}
