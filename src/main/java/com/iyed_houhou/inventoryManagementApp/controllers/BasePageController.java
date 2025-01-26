package com.iyed_houhou.inventoryManagementApp.controllers;

import com.iyed_houhou.inventoryManagementApp.Main;
import com.iyed_houhou.inventoryManagementApp.config.AppConfig;
import com.iyed_houhou.inventoryManagementApp.utils.SessionManager;
import com.iyed_houhou.inventoryManagementApp.utils.UIUtils;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.Region;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BasePageController {

    // Logger instance
    private static final Logger logger = Logger.getLogger(BasePageController.class.getName());

    @FXML
    protected Button adminOnlyPage;

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
            showAlert("Info", "You have been logged out.", Alert.AlertType.WARNING);

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

            BasePageController controller = fxmlLoader.getController();
            if (controller instanceof ProductsPageViewController) {
                ((ProductsPageViewController) controller).refreshData();
            }

            Stage currentStage = AppConfig.MAIN_PRIMARY_STAGE;

            if (currentStage == null) {
                throw new IllegalStateException("Stage is not initialized.");
            }

            // Get the current scene
            Scene currentScene = currentStage.getScene();
            if (currentScene == null) {
                throw new IllegalStateException("Scene is not initialized.");
            }

            boolean isFullScreen = currentStage.isFullScreen();

            if (!isFullScreen) {
                System.out.println("not fullscreen");
                // Center the stage on the screen when exiting full-screen
                Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
                currentStage.setWidth(setPrefWidthToScreen((Region) currentScene.getRoot()));
                currentStage.setHeight(setPrefHeightToScreen((Region) currentScene.getRoot()));
                currentStage.setX((screenBounds.getWidth() - currentStage.getWidth()) / 2);
                currentStage.setY((screenBounds.getHeight() - currentStage.getHeight()) / 2);
            }

            // Update the root of the current scene instead of creating a new scene
            currentScene.setRoot(newRoot);

            logger.info("Page loaded successfully: " + pageName);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to load the page: " + pageName, e);


            showAlert("Error", "Unable to load the requested page. Please contact support." , Alert.AlertType.ERROR);
        }
    }

    protected void showAlert(String title, String message, Alert.AlertType type) {
        UIUtils.showAlert(title,message,type);
    }

    private double setPrefHeightToScreen(Region region) {
        // Get the screen's dimensions
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

        // Set the region's preferred height to the screen height
        region.setPrefHeight(screenBounds.getHeight());
        return screenBounds.getHeight(); // Return the height for use in other methods
    }

    private double setPrefWidthToScreen(Region region) {
        // Get the screen's dimensions
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

        // Set the region's preferred width to the screen width
        region.setPrefWidth(screenBounds.getWidth());
        return screenBounds.getWidth(); // Return the width for use in other methods
    }

    @FXML
    protected void openDashboardPage() {
        loadPage("DashboardPageView.fxml");
    }

    @FXML
    protected void openProductsPage() {
        loadPage("ProductsPageView.fxml");
    }

    @FXML
    protected void openReportsPage() {
        loadPage("ReportsPageView.fxml");
    }

    @FXML
    protected void openSettingsPage() {
        loadPage("SettingsPageView.fxml");
    }

    @FXML
    protected void openAddSalePage() {
        loadPage("AddSalePageView.fxml");
    }

    @FXML
    protected void openSuppliersPage() {
        loadPage("SuppliersPageView.fxml");
    }

    public void openAdminPage() {
        loadPage("AdminPageView.fxml");
    }
}
