package com.iyed_houhou.inventoryManagementApp.services;

import com.iyed_houhou.inventoryManagementApp.config.AppConfig;
import com.iyed_houhou.inventoryManagementApp.models.User;
import com.iyed_houhou.inventoryManagementApp.models.Role;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final Connection connection;

    public UserService() {
        try {
            ensureDatabaseFileExists(); // Ensure the database file exists
            connection = DriverManager.getConnection(AppConfig.DB_URL);
            createUserTableIfNotExists(); // Create the users table if it doesn't exist
        } catch (SQLException e) {
            throw new RuntimeException("Failed to connect to the SQLite database", e);
        }
    }

    private void ensureDatabaseFileExists() {
        File dbFile = new File(AppConfig.DB_PATH);
        if (!dbFile.exists()) {
            try {
                File parentDir = dbFile.getParentFile();
                if (!parentDir.exists()) {
                    Files.createDirectories(parentDir.toPath()); // Ensure the directory exists
                }
                Files.createFile(dbFile.toPath()); // Create the database file
                logger.info("Database file created at: {}", AppConfig.DB_PATH);
            } catch (IOException e) {
                logger.error("Error creating database file at: {}", AppConfig.DB_PATH, e);
                throw new RuntimeException("Failed to create the database file.", e);
            }
        }
    }

    private static final class InstanceHolder {
        private static final UserService instance = new UserService();
    }

    public static UserService getInstance() {
        return InstanceHolder.instance;
    }

    private void createUserTableIfNotExists() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT NOT NULL UNIQUE, " +
                "email TEXT NOT NULL, " +
                "password TEXT NOT NULL, " +
                "role TEXT NOT NULL)";

        try (Statement stmt = connection.createStatement()) {
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

    public Optional<User> getUserByUsername(String username) {
        String query = "SELECT * FROM users WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                String passwordHash = resultSet.getString("password");
                String email = resultSet.getString("email");
                Role role = Role.valueOf(resultSet.getString("role"));
                return Optional.of(new User(username, passwordHash, email, role));
            }
        } catch (SQLException e) {
            logger.error("Error fetching user by username: {}", username, e);
        }
        return Optional.empty();
    }

    public boolean createUser(User user) {
        String query = "INSERT INTO users (username, email, password, role) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, BCrypt.hashpw(user.getPassword(), BCrypt.gensalt())); // Hash password
            stmt.setString(4, user.getRole().name());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.error("Error creating user: {}", user.getUsername(), e);
        }
        return false;
    }

    public boolean updateUser(User user) {
        String query = "UPDATE users SET email = ?, password = ?, role = ? WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, user.getEmail());
            stmt.setString(2, BCrypt.hashpw(user.getPassword(), BCrypt.gensalt())); // Update hashed password
            stmt.setString(3, user.getRole().name());
            stmt.setString(4, user.getUsername());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.error("Error updating user: {}", user.getUsername(), e);
        }
        return false;
    }

    public boolean deleteUser(String username) {
        String query = "DELETE FROM users WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.error("Error deleting user: {}", username, e);
        }
        return false;
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users";
        try (Statement stmt = connection.createStatement()) {
            ResultSet resultSet = stmt.executeQuery(query);

            while (resultSet.next()) {
                String username = resultSet.getString("username");
                String passwordHash = resultSet.getString("password");
                String email = resultSet.getString("email");
                Role role = Role.valueOf(resultSet.getString("role"));

                users.add(new User(username, passwordHash, email, role));
            }
        } catch (SQLException e) {
            logger.error("Error fetching all users", e);
        }
        return users;
    }

    public boolean validateUserCredentials(String username, String password) {
        Optional<User> optionalUser = getUserByUsername(username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return BCrypt.checkpw(password, user.getPassword());
        }
        return false;
    }

    public boolean updateUserRole(User user) {
        String query = "UPDATE users SET role = ? WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(3, user.getRole().name());
            stmt.setString(4, user.getUsername());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.error("Error updating user: {}", user.getUsername(), e);
        }
        return false;
    }
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            logger.error("Error closing database connection", e);
        }
    }
}
