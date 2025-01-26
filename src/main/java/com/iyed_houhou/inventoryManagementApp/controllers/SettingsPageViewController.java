package com.iyed_houhou.inventoryManagementApp.controllers;

import com.iyed_houhou.inventoryManagementApp.models.User;
import com.iyed_houhou.inventoryManagementApp.utils.SessionManager;
import com.iyed_houhou.inventoryManagementApp.utils.ValidationUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class SettingsPageViewController extends BasePageController {

    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TextField addressField;
    @FXML private PasswordField currentPasswordField;
    @FXML private PasswordField newPasswordField;
    @FXML private PasswordField confirmPasswordField;

    private User currentUser;

    @FXML
    public void initialize() {
        currentUser = SessionManager.getInstance().getLoggedInUser();
        populateUserData();
    }

    private void populateUserData() {
        if(currentUser != null) {
            usernameField.setText(currentUser.getUsername());
            emailField.setText(currentUser.getEmail());
            phoneField.setText(currentUser.getPhoneNumber());
            addressField.setText(currentUser.getAddress());
        }
    }

    @FXML
    private void saveSettings() {
        if(!validateInputs()) return;

        updateUserDetails();
        showSuccessAlert();
        clearPasswordFields();
    }

    private boolean validateInputs() {
        // Validate password fields if any are filled
        if(!newPasswordField.getText().isEmpty() || !confirmPasswordField.getText().isEmpty()) {
            if(currentPasswordField.getText().isEmpty()) {
                showAlert("Validation Error", "Current password is required for password change", Alert.AlertType.ERROR);
                return false;
            }

            if(!newPasswordField.getText().equals(confirmPasswordField.getText())) {
                showAlert("Validation Error", "New passwords do not match", Alert.AlertType.ERROR);
                return false;
            }

            if(!validateCurrentPassword()) {
                showAlert("Validation Error", "Current password is incorrect", Alert.AlertType.ERROR);
                return false;
            }
        }

        if(!ValidationUtils.isValidEmail(emailField.getText())) {
            showAlert("Validation Error", "Invalid email format", Alert.AlertType.ERROR);
            return false;
        }

        return true;
    }

    private boolean validateCurrentPassword() {
        // In real implementation, compare hashed passwords
        return currentPasswordField.getText().equals(currentUser.getPassword());
    }

    private void updateUserDetails() {
        // Update basic info
        if(!usernameField.getText().isEmpty()) {
            currentUser.setUsername(usernameField.getText().trim());
        }

        if(!emailField.getText().isEmpty()) {
            currentUser.setEmail(emailField.getText().trim());
        }

        if(!phoneField.getText().isEmpty()) {
            currentUser.setPhoneNumber(phoneField.getText().trim());
        }

        if(!addressField.getText().isEmpty()) {
            currentUser.setAddress(addressField.getText().trim());
        }

        // Update password if changed
        if(!newPasswordField.getText().isEmpty()) {
            currentUser.setPassword(newPasswordField.getText());
        }

        // Update in session and persistence
        SessionManager.getInstance().updateUser(currentUser);
    }

    private void showSuccessAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText("Settings updated successfully!");
        alert.showAndWait();
    }

    private void clearPasswordFields() {
        currentPasswordField.clear();
        newPasswordField.clear();
        confirmPasswordField.clear();
    }

}