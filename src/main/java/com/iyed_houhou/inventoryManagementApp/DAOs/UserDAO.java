package com.iyed_houhou.inventoryManagementApp.DAOs;

import com.iyed_houhou.inventoryManagementApp.config.AppConfig;
import com.iyed_houhou.inventoryManagementApp.config.DatabaseConnectionManager;
import com.iyed_houhou.inventoryManagementApp.models.User;
import com.iyed_houhou.inventoryManagementApp.models.Role;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDAO {
    private static final Logger LOGGER = Logger.getLogger(UserDAO.class.getName());
    private static UserDAO instance;
    private final DatabaseConnectionManager connectionManager;

    private UserDAO() {
        this.connectionManager = DatabaseConnectionManager.getInstance();
        createUserTableIfNotExists();
    }

    public static UserDAO getInstance() {
        if (instance == null) {
            instance = new UserDAO();
        }
        return instance;
    }

    public Optional<User> getUserByUsername(String username) {
        String query = "SELECT * FROM users WHERE username = ?";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, username);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                String passwordHash = resultSet.getString("password");
                String email = resultSet.getString("email");
                Role role = Role.valueOf(resultSet.getString("role"));
                return Optional.of(new User(username, passwordHash, email, role));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching user by username: " + username, e);
        }
        return Optional.empty();
    }

    public boolean createUser(User user) {
        String query = "INSERT INTO users (username, email, password, role) VALUES (?, ?, ?, ?)";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
            stmt.setString(4, user.getRole().name());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating user: " + user.getUsername(), e);
        }
        return false;
    }

    public boolean validateUserCredentials(String username, String password) {
        Optional<User> optionalUser = getUserByUsername(username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return BCrypt.checkpw(password, user.getPassword());
        }
        return false;
    }

    private void createUserTableIfNotExists() {
        org.slf4j.Logger logger = LoggerFactory.getLogger(AppConfig.class);
        String createTableSQL = "CREATE TABLE IF NOT EXISTS users (" +
                "userId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT NOT NULL UNIQUE, " +
                "email TEXT NOT NULL, " +
                "password TEXT NOT NULL, " +
                "role TEXT NOT NULL)";

        try (Connection connection = connectionManager.getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.execute(createTableSQL);

            // Insert the default admin user if no users exist
            String selectQuery = "SELECT COUNT(*) FROM users";
            try (ResultSet resultSet = stmt.executeQuery(selectQuery)) {
                if (resultSet.next() && resultSet.getInt(1) == 0) {
                    String adminPasswordHash = BCrypt.hashpw("Hhmiyed1_", BCrypt.gensalt());
                    String insertAdminSQL = "INSERT INTO users (username, email, password, role) VALUES (?, ?, ?, ?)";
                    try (PreparedStatement insertStmt = connection.prepareStatement(insertAdminSQL)) {
                        insertStmt.setString(1, "admin");
                        insertStmt.setString(2, "houhou.med.iyed@gmail.com");
                        insertStmt.setString(3, adminPasswordHash);
                        insertStmt.setString(4, "SuperAdmin");
                        insertStmt.executeUpdate();
                        logger.info("Default admin user created with username 'admin'");
                    }
                }
            }
        } catch (SQLException e) {
            logger.error("Error creating users table or inserting default admin", e);
        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users";
        try (Connection connection = connectionManager.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet resultSet = stmt.executeQuery(query)) {

            while (resultSet.next()) {
                int userId = resultSet.getInt("userId");
                String username = resultSet.getString("username");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                Role role = Role.valueOf(resultSet.getString("role"));
                users.add(new User(userId, username, password, email, role));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching all users", e);
        }
        return users;
    }

    public void updateUser(User user) {
        String query = "UPDATE users SET email = ?, role = ? WHERE username = ?";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getRole().name());
            stmt.setString(3, user.getUsername());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                LOGGER.info("User updated successfully: " + user.getUsername());
            } else {
                LOGGER.warning("No user found to update with username: " + user.getUsername());
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating user: " + user.getUsername(), e);
        }
    }

    public void deleteUser(int userId) {
        String query = "DELETE FROM users WHERE userId = ?";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, userId);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                LOGGER.info("User deleted successfully with ID: " + userId);
            } else {
                LOGGER.warning("No user found to delete with ID: " + userId);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting user with ID: " + userId, e);
        }
    }


    public User getUserById(int userId) {
        String query = "SELECT * FROM users WHERE userId = ?";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, userId);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                String username = resultSet.getString("username");
                String passwordHash = resultSet.getString("password");
                String email = resultSet.getString("email");
                Role role = Role.valueOf(resultSet.getString("role"));
                return new User(userId,username, passwordHash, email, role);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching user by userId: " + userId, e);
        }
        return null;
    }
}
