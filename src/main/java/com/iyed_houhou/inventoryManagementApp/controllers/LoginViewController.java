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
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/iyed_houhou/inventoryManagementApp/view/DashboardView.fxml"));
            loadPage("DashboardView.fxml");
    }

    @FXML
    private void openRegister() {
            // Load register view
            loadPage("RegisterView.fxml");
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

}
