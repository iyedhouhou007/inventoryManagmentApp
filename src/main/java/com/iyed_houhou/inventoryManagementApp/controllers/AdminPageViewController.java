package com.iyed_houhou.inventoryManagementApp.controllers;

import com.iyed_houhou.inventoryManagementApp.customFxmlNodes.UserCard;
import com.iyed_houhou.inventoryManagementApp.managers.UserListManager;
import com.iyed_houhou.inventoryManagementApp.models.User;
import com.iyed_houhou.inventoryManagementApp.utils.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;

import java.util.List;

public class AdminPageViewController extends BasePageController{

    @FXML
    private Label noUserLabel;
    @FXML
    private FlowPane usersContainer;
    private final UserListManager userListManager = UserListManager.getInstance();

    @FXML
    public void initialize(){
        List<User> initialUsersList = userListManager.getUserList();
        // Get the logged-in user's role once
        User loggedInUser = SessionManager.getInstance().getLoggedInUser();

        for (User user : initialUsersList) {
            // Skip users with higher privilege than the logged-in user
            if ((user.getRole().compareTo(loggedInUser.getRole()) < 0) || loggedInUser.getUsername().equals(user.getUsername())) {
                continue;
            }
            addToUsersContainer(user);
        }
        
        if(usersContainer.getChildren().isEmpty()){
            noUserLabel.setVisible(usersContainer.getChildren().isEmpty());
        }
    }

    private void addToUsersContainer(User user) {
        UserCard userCard = new UserCard(user);
        usersContainer.getChildren().add(userCard);
        userCard.setCursor(Cursor.cursor("hand"));

    }


}
