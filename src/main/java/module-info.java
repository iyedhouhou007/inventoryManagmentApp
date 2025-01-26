module com.iyed_houhou.inventoryManagementApp {
    requires javafx.controls;
    requires javafx.fxml;
    requires jbcrypt;
    requires org.slf4j;
    requires java.sql;


    opens com.iyed_houhou.inventoryManagementApp to javafx.fxml;
    exports com.iyed_houhou.inventoryManagementApp.models;
    exports com.iyed_houhou.inventoryManagementApp.utils;
    exports com.iyed_houhou.inventoryManagementApp;
    exports com.iyed_houhou.inventoryManagementApp.controllers;
    opens com.iyed_houhou.inventoryManagementApp.controllers to javafx.fxml;
    exports com.iyed_houhou.inventoryManagementApp.managers;
    exports com.iyed_houhou.inventoryManagementApp.services;
    exports com.iyed_houhou.inventoryManagementApp.customFxmlNodes;
    opens com.iyed_houhou.inventoryManagementApp.customFxmlNodes to javafx.fxml;
    exports com.iyed_houhou.inventoryManagementApp.customFxmlNodes.controllers;
    opens com.iyed_houhou.inventoryManagementApp.customFxmlNodes.controllers to javafx.fxml;

}