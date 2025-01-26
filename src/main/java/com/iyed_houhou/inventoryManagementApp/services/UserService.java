package com.iyed_houhou.inventoryManagementApp.services;

import com.iyed_houhou.inventoryManagementApp.DAOs.UserDAO;
import com.iyed_houhou.inventoryManagementApp.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserDAO userDAO;
    private static UserService instance;

    private UserService() {
        this.userDAO = UserDAO.getInstance();
    }

    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    // Get a list of all users from the database
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    // Add a new user
    public boolean createUser(User user) {
        // Business logic for creating a user (e.g., validation)
        if (user.getUsername().isEmpty() || user.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Username and password cannot be empty.");
        }

        boolean isCreated = userDAO.createUser(user);
        if (isCreated) {
            logger.info("User created successfully: {}", user.getUsername());
        } else {
            logger.error("Failed to create user: {}", user.getUsername());
        }
        return isCreated;
    }

    // Get user by username
    public Optional<User> getUserByUsername(String username) {
        return userDAO.getUserByUsername(username);
    }

    // Validate user credentials
    public boolean validateUserCredentials(String username, String password) {
        return userDAO.validateUserCredentials(username, password);
    }

    // Update user information
    public void updateUser(User user) {
        userDAO.updateUser(user);
        logger.info("User updated successfully: {}", user.getUsername());
    }

    // Delete a user by ID
    public void deleteUser(int userId) {
        userDAO.deleteUser(userId);
        logger.info("User deleted successfully with ID: {}", userId);
    }

    public List<User> getAllUsersInDB() {
        return userDAO.getAllUsers();
    }

    public void addUser(User user) {
        userDAO.createUser(user);
    }

    public User getUserById(int userId) {
        return userDAO.getUserById(userId);
    }

    public List<User> refreshUsersList() {
        return getAllUsers();
    }
}
