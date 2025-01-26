package com.iyed_houhou.inventoryManagementApp.customFxmlNodes;

import com.iyed_houhou.inventoryManagementApp.config.AppConfig;
import com.iyed_houhou.inventoryManagementApp.customFxmlNodes.controllers.UserDetailsController;
import com.iyed_houhou.inventoryManagementApp.managers.UserListManager;
import com.iyed_houhou.inventoryManagementApp.models.User;
import com.iyed_houhou.inventoryManagementApp.services.UserService;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserCard extends VBox {
    private User user;
    private final UserService userService;
    private static final Logger logger = Logger.getLogger(UserCard.class.getName());

    public UserCard(User user) {
        this.user = user;
        this.userService = UserService.getInstance();

        setupCardLayout();
        initializeUIComponents();
        setupRemoveButton();

        // Add click event handler
        this.setOnMouseClicked(this::handleClick);
    }

    private void handleClick(MouseEvent mouseEvent) {
        System.out.println("User clicked: " + user.getUsername());
        showUserDetails();
    }

    private void showUserDetails() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(AppConfig.CUSTOM_DIALOG_PATH + "UserCardDetailsView.fxml"));
            DialogPane dialogPane = loader.load();

            UserDetailsController controller = loader.getController();
            controller.setUser(user);

            Dialog<Void> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("User Details");
            dialog.initOwner(AppConfig.MAIN_PRIMARY_STAGE);
            dialog.initModality(Modality.APPLICATION_MODAL);

            // Style dialog buttons
            Button closeButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.CLOSE);
            if (closeButton != null) {
                closeButton.getStyleClass().add("close-btn");
            }

            Button applyButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.APPLY);
            if (applyButton != null) {
                applyButton.getStyleClass().add("save-button");
            }

            Optional<Void> result = dialog.showAndWait();
            if (result.toString().contains("Apply")) {
                controller.saveUserDetails();
                refreshUI();
            }

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to load UserCardDetailsView.fxml", e);
        }
    }

    private void refreshUI() {
        getChildren().clear();
        initializeUIComponents();
        setupRemoveButton();
    }

    private void setupCardLayout() {
        setAlignment(Pos.CENTER);
        setSpacing(10);
        setMaxWidth(250);
        setMinWidth(150);
        getStyleClass().add("user-card");
    }

    private void initializeUIComponents() {
        // User ID
        Label userIdLabel = new Label("User ID: " + user.getUserId());
        userIdLabel.getStyleClass().add("user-id");

        // Username
        Text usernameText = new Text(user.getUsername());
        usernameText.getStyleClass().add("user-name");

        // Email
        Text emailText = new Text("Email: " + user.getEmail());
        emailText.getStyleClass().add("user-email");

        // Role
        Text roleText = new Text("Role: " + user.getRole());
        roleText.getStyleClass().add("user-role");

        // Phone Number
        String phone = user.getPhoneNumber() != null ? user.getPhoneNumber() : "N/A";
        Text phoneText = new Text("Phone: " + phone);
        phoneText.getStyleClass().add("user-phone");

        // Address
        String address = user.getAddress() != null ? user.getAddress() : "N/A";
        Text addressText = new Text("Address: " + address);
        addressText.getStyleClass().add("user-address");

        getChildren().addAll(userIdLabel, usernameText, emailText,
                roleText, phoneText, addressText);
    }

    private void setupRemoveButton() {
        Button removeButton = new Button("Remove User");
        removeButton.getStyleClass().add("remove-user-btn");
        removeButton.setOnAction(_ -> handleUserRemoval());
        getChildren().add(removeButton);
    }

    private void handleUserRemoval() {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirm Removal");
        confirmation.setHeaderText("Delete User: " + user.getUsername());
        confirmation.setContentText("This action cannot be undone");
        confirmation.initOwner(AppConfig.MAIN_PRIMARY_STAGE);
        confirmation.initModality(Modality.APPLICATION_MODAL);

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                userService.deleteUser(user.getUserId());
                removeFromUI();
            } catch (Exception e) {
                logger.log(Level.SEVERE, "User deletion failed", e);
                showErrorAlert();
            }
        }
    }

    private void removeFromUI() {
        if (getParent() instanceof Pane parent) {
            parent.getChildren().remove(this);
        }
    }

    private void showErrorAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText("Failed to delete user. Please check logs.");
        alert.initOwner(AppConfig.MAIN_PRIMARY_STAGE);
        alert.showAndWait();
    }

    public void refreshUserInfo() {
        refreshUI();
    }

    public User getUser() {
        return user;
    }

    public void updateUser(User updatedUser) {
        this.user = updatedUser;
        UserListManager userListManager = UserListManager.getInstance();
        userListManager.updateUser(updatedUser);
        refreshUI();
    }
}