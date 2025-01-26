package com.iyed_houhou.inventoryManagementApp.controllers;


import com.iyed_houhou.inventoryManagementApp.models.Role;
import com.iyed_houhou.inventoryManagementApp.models.User;
import com.iyed_houhou.inventoryManagementApp.utils.SessionManager;
import javafx.fxml.FXML;

public class ReportsPageViewController extends BasePageController {

    @FXML
    public void initialize() {
        User loggedInUser = SessionManager.getInstance().getLoggedInUser();
        if (loggedInUser == null || (!loggedInUser.getRole().equals(Role.Admin) && !loggedInUser.getRole().equals(Role.SuperAdmin)) ) {
            adminOnlyPage.setVisible(false);
        }}
}
