package com.iyed_houhou.inventoryManagementApp.controllers;

import com.iyed_houhou.inventoryManagementApp.models.Role;
import com.iyed_houhou.inventoryManagementApp.models.User;
import com.iyed_houhou.inventoryManagementApp.services.UserService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterViewController {
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Pane registerPane;

    // Use the singleton instance of UserService
    private final UserService userService = UserService.getInstance();

    @FXML
    private void register() throws IOException {
        String username = usernameField.getText().trim();
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Check if fields are empty
        if (username.isEmpty() || password.isEmpty() || email.isEmpty() || confirmPassword.isEmpty()) {
            showAlert("Error", "All fields must be filled.", Alert.AlertType.ERROR);
            return;
        }

        // Validate email format
        if (!isValidEmail(email)) {
            showAlert("Error", "Please enter a valid email address.", Alert.AlertType.ERROR);
            return;
        }

        // Check if passwords match
        if (!password.equals(confirmPassword)) {
            showAlert("Error", "Passwords do not match.", Alert.AlertType.ERROR);
            return;
        }

        // Register user logic (using UserService)
        User newUser = new User(username, password, email, Role.NotApproved);
        boolean registrationSuccessful = userService.createUser(newUser);  // Default role can be Assistant

        if (registrationSuccessful) {
            showAlert("Registration Successful", "User " + username + " registered successfully!", Alert.AlertType.INFORMATION);
            backToLogin();  // Redirect to login page
        } else {
            showAlert("Registration Failed", "Username already exists.", Alert.AlertType.ERROR);
        }
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


    @FXML
    private void backToLogin() throws IOException {
        registerPane.getScene().setRoot(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/iyed_houhou/inventoryManagementApp/view/LoginView.fxml"))));
    }

    // Email validation method
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
