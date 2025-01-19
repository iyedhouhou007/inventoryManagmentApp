package com.iyed_houhou.inventoryManagementApp.services;

import com.iyed_houhou.inventoryManagementApp.repositories.UserDAO;
import com.iyed_houhou.inventoryManagementApp.models.User;

import java.util.Optional;

public class UserService {

    private static final UserDAO userDAO = new UserDAO();

    private UserService() { }

    private static final class InstanceHolder {
        private static final UserService instance = new UserService();
    }

    public static UserService getInstance() {
        return InstanceHolder.instance;
    }

    public Optional<User> getUserByUsername(String username) {
        return userDAO.getUserByUsername(username);
    }

    public boolean createUser(User user) {
        return userDAO.createUser(user);
    }

    public boolean validateUserCredentials(String username, String password) {
        return userDAO.validateUserCredentials(username, password);
    }
}
