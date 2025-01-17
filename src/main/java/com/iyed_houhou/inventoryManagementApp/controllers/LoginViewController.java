package com.iyed_houhou.inventoryManagementApp.controllers;

import com.iyed_houhou.inventoryManagementApp.Main;
import com.iyed_houhou.inventoryManagementApp.models.Role;
import com.iyed_houhou.inventoryManagementApp.models.User;
import com.iyed_houhou.inventoryManagementApp.services.UserService;
import com.iyed_houhou.inventoryManagementApp.utils.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

public class LoginViewController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    // Use the singleton instance of UserService
    private final UserService userService = UserService.getInstance();

    @FXML
    private void login() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Login Failed", "Please enter both username and password.", Alert.AlertType.ERROR);
            return;
        }

        Optional<User> optionalUser = userService.getUserByUsername(username);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            if (userService.validateUserCredentials(username, password)) {

                if(user.getRole() == Role.NotApproved) {
                    showAlert("Login Failed", "you are not yet approved wait for the owner or contact admin '0549845542'", Alert.AlertType.ERROR);
                }else {
                    // Save the user in the SessionManager
                    SessionManager.getInstance().setLoggedInUser(user);

                    showAlert("Login Successful", "Welcome, " + username + "!", Alert.AlertType.INFORMATION);

                    usernameField.clear();
                    passwordField.clear();

                    // Open the home page
                    openHomePage();
                }
            } else {
                showAlert("Login Failed", "Invalid username or password.", Alert.AlertType.ERROR);
                usernameField.clear();
                passwordField.clear();
            }
        } else {
            showAlert("Login Failed", "Invalid username or password.", Alert.AlertType.ERROR);
            usernameField.clear();
            passwordField.clear();
        }
    }


    private void openHomePage() {
        try {
            // Load HomePage FXML
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/iyed_houhou/inventoryManagementApp/view/DashboardView.fxml"));
            Scene homeScene = new Scene(fxmlLoader.load());

            // Get current stage and set the new scene
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(homeScene);

            stage.setFullScreen(true);
            homeScene.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.F11) {
                    stage.setFullScreen(!stage.isFullScreen());
                }
            });
            stage.show();
        } catch (IOException e) {
            showAlert("Error", "Could not load home page.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void openRegister() {
        try {
            // Load register view
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/iyed_houhou/inventoryManagementApp/view/RegisterView.fxml"));
            Pane registerPane = fxmlLoader.load();  // Load the FXML content

            // Get the current stage and set the new scene with the register pane
            Stage stage = getStage(registerPane);
            stage.show();  // Show the stage
        } catch (IOException e) {
            showAlert("Error", "Failed to load the register page.", Alert.AlertType.ERROR);
        }
    }

    private Stage getStage(Pane registerPane) {
        Stage stage = (Stage) usernameField.getScene().getWindow();  // Get the current stage
        Scene registerScene = new Scene(registerPane);  // Create a new scene with the register pane
        stage.setScene(registerScene);  // Set the new scene

        stage.setFullScreen(true);
        registerScene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.F11) {
                stage.setFullScreen(!stage.isFullScreen());
            }
        });
        return stage;
    }

    @FXML
    private void forgotPassword() {
        // Handle the forgot password logic here (e.g., show a password recovery screen)
        showAlert("Forgot Password", "Contact Admin nbr: '0549845542', email: 'houhou.med.iyed@gmai.com'", Alert.AlertType.INFORMATION);
    }


    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        // Apply CSS styles to the alert
        alert.getDialogPane().getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/iyed_houhou/inventoryManagementApp/styles/alertStyles.css")).toExternalForm());

        alert.showAndWait();
    }
}
