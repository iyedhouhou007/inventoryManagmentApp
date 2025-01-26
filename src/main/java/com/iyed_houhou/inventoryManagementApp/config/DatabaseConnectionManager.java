package com.iyed_houhou.inventoryManagementApp.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionManager {
    private static final String DB_URL = AppConfig.DB_URL;
    private static DatabaseConnectionManager instance;

    private DatabaseConnectionManager() {
        // Private constructor to enforce Singleton pattern
    }

    public static DatabaseConnectionManager getInstance() {
        if (instance == null) {
            synchronized (DatabaseConnectionManager.class) {
                if (instance == null) {
                    instance = new DatabaseConnectionManager();
                }
            }
        }
        return instance;
    }

    // Get a new database connection
    public Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(DB_URL);
        connection.createStatement().execute("PRAGMA foreign_keys = ON;"); // Enable foreign key support for SQLite
        return connection;
    }
}
