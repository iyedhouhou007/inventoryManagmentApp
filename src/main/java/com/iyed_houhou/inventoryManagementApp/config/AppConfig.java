package com.iyed_houhou.inventoryManagementApp.config;

import javafx.stage.Stage;

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
}
