package com.iyed_houhou.inventoryManagementApp.managers;

import com.iyed_houhou.inventoryManagementApp.models.User;
import com.iyed_houhou.inventoryManagementApp.services.UserService;

import java.util.List;

public class UserListManager {

    private static UserListManager instance;
    private final UserService userService;
    private List<User> userList;

    private UserListManager() {
        userService = UserService.getInstance();
        userList = userService.getAllUsersInDB();  // Load initial list from DB
    }

    public static UserListManager getInstance() {
        if (instance == null) {
            instance = new UserListManager();
        }
        return instance;
    }

    // Fetches all users from the database through UserService
    public List<User> getUserList() {
        return userList;  // Return the local user list managed by this class
    }

    // Adds a user to the list and the database
    public void addUser(User user) {
        // Add the user to the local list
        userList.add(user);

        // Add the user to the database using UserService
        userService.addUser(user);
    }

    // Removes a user from the list and the database
    public void removeUser(User user) {
        // Remove the user from the local list
        userList.remove(user);

        // Remove the user from the database using UserService
        userService.deleteUser(user.getUserId());
    }

    // Updates a user in the list and in the database
    public void updateUser(User user) {
        // Update the user in the local list
        int index = getUserIndex(user.getUserId());
        if (index != -1) {
            userList.set(index, user);
        }

        // Update the user in the database using UserService
        userService.updateUser(user);
    }

    // Searches for a user by username in the local list
    public User getUserByUsername(String username) {
        for (User u : userList) {
            if (u.getUsername().equals(username)) {
                return u;  // Return the user if found
            }
        }
        return null;  // Return null if user not found
    }

    public User getUserById(int userId) {
        return userService.getUserById(userId);
    }

    // Helper method to find the index of a user by their ID
    private int getUserIndex(int userId) {
        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).getUserId() == userId) {
                return i;
            }
        }
        return -1;  // Return -1 if user is not found
    }

    public void refreshUsersList() {
        // Refresh the local user list by fetching the updated list from the database
        userList = userService.refreshUsersList();
    }
}
