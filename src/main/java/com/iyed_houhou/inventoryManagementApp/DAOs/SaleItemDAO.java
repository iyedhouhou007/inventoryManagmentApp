package com.iyed_houhou.inventoryManagementApp.DAOs;

import com.iyed_houhou.inventoryManagementApp.config.DatabaseConnectionManager;
import com.iyed_houhou.inventoryManagementApp.managers.ProductListManager;
import com.iyed_houhou.inventoryManagementApp.models.Product;
import com.iyed_houhou.inventoryManagementApp.models.SaleItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SaleItemDAO {
    private static final Logger logger = LoggerFactory.getLogger(SaleItemDAO.class.getName());
    private static SaleItemDAO instance;

    private SaleItemDAO() {
        createSaleItemTableIfNotExists(); // Ensure the table exists
    }

    public static SaleItemDAO getInstance() {
        if (instance == null) {
            synchronized (SaleItemDAO.class) {
                if (instance == null) {
                    instance = new SaleItemDAO();
                }
            }
        }
        return instance;
    }

    // Create SaleItem table if it doesn't exist
    private void createSaleItemTableIfNotExists() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS saleItem (" +
                "saleItemId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "saleId INTEGER, " +
                "productId INTEGER, " +
                "quantity INTEGER NOT NULL, " +
                "totalAmount REAL NOT NULL, " +
                "FOREIGN KEY (saleId) REFERENCES Sale(saleId) ON DELETE CASCADE, " +
                "FOREIGN KEY (productId) REFERENCES Product(productId)" +
                ");";

        try (Connection connection = DatabaseConnectionManager.getInstance().getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(createTableSQL);
            logger.info("SaleItem table created or already exists.");
        } catch (SQLException e) {
            logger.error("Error creating SaleItem table", e);
        }
    }

    // Retrieve SaleItems related to a specific Sale ID
    public List<SaleItem> getSalesItemsRelatedToSale(int saleId) {
        List<SaleItem> saleItems = new ArrayList<>();
        String query = "SELECT si.saleItemId, si.productId, si.quantity, si.totalAmount " +
                "FROM saleItem si WHERE si.saleId = ?";

        try (Connection connection = DatabaseConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, saleId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int productId = rs.getInt("productId");
                    int quantity = rs.getInt("quantity");
                    BigDecimal totalAmount = rs.getBigDecimal("totalAmount");

                    // Fetch the product associated with the SaleItem
                    ProductListManager productListManager = ProductListManager.getInstance();
                    Product product = productListManager.getProductById(productId);

                    if (product != null) {
                        SaleItem saleItem = new SaleItem(saleId, new Date(), product, quantity);
                        saleItem.setTotalAmount(totalAmount);
                        saleItems.add(saleItem);
                    } else {
                        logger.warn("Product with ID {} not found in ProductListManager", productId);
                    }
                }
            }
        } catch (SQLException e) {
            logger.error("Error retrieving SaleItems for SaleId: {}", saleId, e);
        }
        return saleItems;
    }

    // Add a SaleItem to the database
    public void addSaleItem(SaleItem saleItem) {
        String insertSQL = "INSERT INTO saleItem (saleId, productId, quantity, totalAmount) VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(insertSQL)) {

            stmt.setInt(1, saleItem.getSaleId());
            stmt.setInt(2, saleItem.getProduct().getProductId());
            stmt.setInt(3, saleItem.getQuantity());
            stmt.setBigDecimal(4, saleItem.getTotalAmount());

            stmt.executeUpdate();
            logger.info("SaleItem added successfully: {}", saleItem);
        } catch (SQLException e) {
            logger.error("Error adding SaleItem", e);
        }
    }


}
