package com.iyed_houhou.inventoryManagementApp.config;

import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class AppConfig {
    // The database connection URL and file path
    public static final String DB_FILE_NAME = "inventory_management.db";
    public static final String DB_FOLDER_PATH = System.getenv("APPDATA") + "\\InventoryManagementApp";
    public static final String DB_PATH = DB_FOLDER_PATH + "\\" + DB_FILE_NAME;
    public static final String DB_URL = "jdbc:sqlite:" + DB_PATH; // SQLite connection URL

    public static Stage MAIN_PRIMARY_STAGE;

    public static String STYLES_PATH = "/com/iyed_houhou/inventoryManagementApp/styles/";
    public static String VIEW_PATH = "/com/iyed_houhou/inventoryManagementApp/view/navigation/";
    public static String CUSTOM_DIALOG_PATH = "/com/iyed_houhou/inventoryManagementApp/view/customDialogs/";


    public static void createDatabaseIfNotExists() {
        File dbFile = new File(AppConfig.DB_PATH);
        Logger logger = LoggerFactory.getLogger(AppConfig.class);
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
}