package com.iyed_houhou.inventoryManagementApp.controllers;

import com.iyed_houhou.inventoryManagementApp.models.Role;
import com.iyed_houhou.inventoryManagementApp.models.User;
import com.iyed_houhou.inventoryManagementApp.services.UserService;
import com.iyed_houhou.inventoryManagementApp.utils.SessionManager;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.util.Optional;

public class LoginViewController extends BasePageController{
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
            // Load HomePage FXML
            loadPage("DashboardView.fxml");
    }

    @FXML
    private void openRegister() {
            // Load register view
            loadPage("RegisterView.fxml");
    }


    @FXML
    private void forgotPassword() {
        // Handle the forgot password logic here (e.g., show a password recovery screen)
        showAlert("Forgot Password", "Contact Admin nbr: '0549845542', email: 'houhou.med.iyed@gmai.com'", Alert.AlertType.INFORMATION);
    }

}
