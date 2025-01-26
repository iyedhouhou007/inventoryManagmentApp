package com.iyed_houhou.inventoryManagementApp.utils;

import com.iyed_houhou.inventoryManagementApp.models.Role;
import com.iyed_houhou.inventoryManagementApp.models.User;

public class SessionManager {

    private static SessionManager instance;
    private User loggedInUser;

    private SessionManager() {
        // Private constructor to prevent instantiation
//        this.loggedInUser = new User("iyed","","", Role.SuperAdmin);
    }

    // Singleton instance
    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    // Set the logged-in user
    public void setLoggedInUser(User user) {
        this.loggedInUser = user;
    }

    // Get the logged-in user
    public User getLoggedInUser() {
        return loggedInUser;
    }

    // Clear session
    public void clearSession() {
        this.loggedInUser = null;
    }

    public void updateUser(User currentUser) {
        this.loggedInUser = currentUser;
    }
}
