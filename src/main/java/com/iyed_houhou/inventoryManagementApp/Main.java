package com.iyed_houhou.inventoryManagementApp;

import com.iyed_houhou.inventoryManagementApp.config.AppConfig;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;

public class Main extends Application {
    private static final Logger logger = LoggerFactory.getLogger(Main.class); // Logger instance

    @Override
    public void start(Stage primaryStage) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(AppConfig.VIEW_PATH + "ProductsPageView.fxml"));
            StackPane root = new StackPane();
            root.getChildren().add(fxmlLoader.load());

            Scene scene = new Scene(root);
            primaryStage.setTitle("Inventory Management App");
            primaryStage.setScene(scene);
            primaryStage.setFullScreen(true);

            // Listen for F11 to toggle full-screen mode
            scene.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.F11) {
                    boolean isFullScreen = primaryStage.isFullScreen();
                    primaryStage.setFullScreen(!isFullScreen);

                    if (isFullScreen) {
                        System.out.println("fullscreen");
                        // Center the stage on the screen when exiting full-screen
                        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
                        primaryStage.setWidth(setPrefWidthToScreen((Region) scene.getRoot()));
                        primaryStage.setHeight(setPrefHeightToScreen((Region) scene.getRoot()));
                        primaryStage.setX((screenBounds.getWidth() - primaryStage.getWidth()) / 2);
                        primaryStage.setY((screenBounds.getHeight() - primaryStage.getHeight()) / 2);
                    }
                }

                if (event.getCode() == KeyCode.ESCAPE){
                    boolean isFullScreen = primaryStage.isFullScreen();

                    if (!isFullScreen) {
                        System.out.println("fullscreen");
                        // Center the stage on the screen when exiting full-screen
                        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
                        primaryStage.setWidth(setPrefWidthToScreen((Region) scene.getRoot()));
                        primaryStage.setHeight(setPrefHeightToScreen((Region) scene.getRoot()));
                        primaryStage.setX((screenBounds.getWidth() - primaryStage.getWidth()) / 2);
                        primaryStage.setY((screenBounds.getHeight() - primaryStage.getHeight()) / 2);
                    }

                }
            });


            AppConfig.MAIN_PRIMARY_STAGE = primaryStage;
            primaryStage.show();
            logger.info("Application started successfully.");
        } catch (IOException e) {
            logger.error("Failed to load ProductsPageView.fxml", e);

            // Show an error alert
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to Load");
            alert.setContentText("Unable to load the ProductsPageView.fxml file. Please contact support.");
            alert.showAndWait();
        }
    }

    public static void main(String[] args) {
        launch();
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

}
