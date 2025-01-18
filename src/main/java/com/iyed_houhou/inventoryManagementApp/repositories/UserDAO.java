package com.iyed_houhou.inventoryManagementApp.dao;

import com.iyed_houhou.inventoryManagementApp.config.AppConfig;
import com.iyed_houhou.inventoryManagementApp.models.User;
import com.iyed_houhou.inventoryManagementApp.models.Role;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.Optional;

public class UserDAO {

    private final Connection connection;

    public UserDAO()  {
        try {
            this.connection = DriverManager.getConnection(AppConfig.DB_URL);
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public boolean createUser(User user) {
        String query = "INSERT INTO users (username, email, password, role) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
            stmt.setString(4, user.getRole().name());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
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
}
