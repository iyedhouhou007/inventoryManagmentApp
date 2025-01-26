package com.iyed_houhou.inventoryManagementApp.customFxmlNodes.controllers;

import com.iyed_houhou.inventoryManagementApp.managers.UserListManager;
import com.iyed_houhou.inventoryManagementApp.models.User;
import com.iyed_houhou.inventoryManagementApp.models.Role;
import com.iyed_houhou.inventoryManagementApp.utils.SessionManager;
import com.iyed_houhou.inventoryManagementApp.utils.ValidationUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class UserDetailsController {
    private User user;

    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TextField addressField;
    @FXML private ComboBox<Role> roleComboBox;

    @FXML
    public void initialize() {
        // Set up role dropdown
        SessionManager sessionManager = SessionManager.getInstance();
        if (sessionManager.getLoggedInUser().getRole() == Role.SuperAdmin) {
            roleComboBox.getItems().setAll(Role.values());
        }else {
            roleComboBox.getItems().setAll(Role.values());
            roleComboBox.getItems().remove(Role.SuperAdmin);
        }
    }

    public void setUser(User user) {
        this.user = user;
        populateFields();
    }

    private void populateFields() {
        if (user != null) {
            usernameField.setText(user.getUsername());
            emailField.setText(user.getEmail());
            phoneField.setText(user.getPhoneNumber());
            addressField.setText(user.getAddress());
            roleComboBox.setValue(user.getRole());
        }
    }

    public void saveUserDetails() {
        if (!validateInputs()) return;

        user.setUsername(usernameField.getText().trim());
        user.setEmail(emailField.getText().trim());

        // Safely handle phone field
        if (phoneField != null) {
            String phoneText = phoneField.getText();
            if (phoneText != null && !phoneText.trim().isEmpty()) {
                user.setPhoneNumber(phoneText.trim());
            }
        }

        // Safely handle address field
        if (addressField != null) {
            String addressText = addressField.getText();
            if (addressText != null && !addressText.trim().isEmpty()) {
                user.setAddress(addressText.trim());
            }
        }

        user.setRole(roleComboBox.getValue());
        UserListManager userListManager = UserListManager.getInstance();
        userListManager.updateUser(user);
    }

    public User getUpdatedUser() {
        return user;
    }

    private boolean validateInputs() {
        if (usernameField.getText().trim().isEmpty()) {
            showAlert("Validation Error", "Username cannot be empty");
            return false;
        }

        if (!ValidationUtils.isValidEmail(emailField.getText().trim())) {
            showAlert("Validation Error", "Invalid email format");
            return false;
        }


        if (roleComboBox.getValue() == null) {
            showAlert("Validation Error", "Role must be selected");
            return false;
        }

        return true;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}