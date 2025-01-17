package com.iyed_houhou.inventoryManagementApp.config;

public class AppConfig {
    // The database connection URL and file path
    public static final String DB_FILE_NAME = "inventory_management.db";
    public static final String DB_FOLDER_PATH = System.getenv("APPDATA") + "\\InventoryManagementApp";
    public static final String DB_PATH = DB_FOLDER_PATH + "\\" + DB_FILE_NAME;
    public static final String DB_URL = "jdbc:sqlite:" + DB_PATH; // SQLite connection URL


    // Example: Max number of users that can be fetched at once
    public static final int MAX_USERS_FETCH_LIMIT = 100;

    // Example: Default language setting
    public static final String DEFAULT_LANGUAGE = "en";

    // You can also store application settings here, such as logging levels or file paths
    public static final String LOGGING_LEVEL = "INFO";

    public static String STORE_NAME ;
}
