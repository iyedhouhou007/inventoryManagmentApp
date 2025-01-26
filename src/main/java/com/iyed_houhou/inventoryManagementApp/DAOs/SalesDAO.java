package com.iyed_houhou.inventoryManagementApp.DAOs;

import com.iyed_houhou.inventoryManagementApp.config.DatabaseConnectionManager;
import com.iyed_houhou.inventoryManagementApp.models.Sale;
import com.iyed_houhou.inventoryManagementApp.models.SaleItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SalesDAO {

    private static final Logger logger = LoggerFactory.getLogger(SalesDAO.class);
    private static SalesDAO instance;
    private final SaleItemDAO saleItemDAO; // Dependency to manage sale items

    private SalesDAO() {
        this.saleItemDAO = SaleItemDAO.getInstance();
        try (Connection connection = DatabaseConnectionManager.getInstance().getConnection()) {
            createSalesTableIfNotExists(connection);
        } catch (SQLException e) {
            logger.error("Failed to initialize SalesDAO", e);
            throw new RuntimeException("Error initializing SalesDAO", e);
        }
    }

    public static synchronized SalesDAO getInstance() {
        if (instance == null) {
            instance = new SalesDAO();
        }
        return instance;
    }

    public List<Sale> getAllSalesFromDB() {
        List<Sale> sales = new ArrayList<>();
        String selectSQL = "SELECT * FROM sales";

        try (Connection connection = DatabaseConnectionManager.getInstance().getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(selectSQL)) {

            while (rs.next()) {
                sales.add(mapToSale(rs));
            }
        } catch (SQLException e) {
            logger.error("Error retrieving all sales", e);
        }
        return sales;
    }

    private Sale mapToSale(ResultSet rs) throws SQLException {
        int saleId = rs.getInt("saleId");
        List<SaleItem> items = saleItemDAO.getSalesItemsRelatedToSale(saleId);
        return new Sale(
                saleId,
                rs.getTimestamp("saleDate"),
                rs.getBigDecimal("totalAmount"),
                items
        );
    }

    private void createSalesTableIfNotExists(Connection connection) {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS sales (" +
                "saleId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "saleDate DATETIME, " +
                "totalAmount REAL" +
                ");";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTableSQL);
            logger.info("Sales table ensured to exist.");
        } catch (SQLException e) {
            logger.error("Error creating sales table", e);
        }
    }

    public int addSale(Sale sale) {
        String insertSaleSQL = "INSERT INTO sales (saleDate, totalAmount) VALUES (?, ?)";
        int saleId = -1;

        try (Connection connection = DatabaseConnectionManager.getInstance().getConnection()) {
            connection.setAutoCommit(false); // Start transaction

            try (PreparedStatement stmt = connection.prepareStatement(insertSaleSQL, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setTimestamp(1, new Timestamp(sale.getSaleDate().getTime()));
                stmt.setBigDecimal(2, sale.getTotalAmount());
                stmt.executeUpdate();

                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        saleId = generatedKeys.getInt(1);
                        sale.setSaleId(saleId);
                    }
                }
            }

            // Add sale items
            for (SaleItem saleItem : sale.getSaleItems()) {
                saleItem.setSaleId(saleId);
                saleItemDAO.addSaleItem(saleItem);
            }

            connection.commit(); // Commit transaction
        } catch (SQLException e) {
            logger.error("Error adding sale", e);
            throw new RuntimeException("Error adding sale", e);
        }

        return saleId;
    }

    public boolean deleteSale(Sale sale) {
        String deleteSaleSQL = "DELETE FROM sales WHERE saleId = ?";

        try (Connection connection = DatabaseConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(deleteSaleSQL)) {
            stmt.setInt(1, sale.getSaleId());
            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                logger.info("Sale with saleId: {} deleted successfully.", sale.getSaleId());
                return true;
            } else {
                logger.warn("No sale found with saleId: {}", sale.getSaleId());
                return false;
            }
        } catch (SQLException e) {
            logger.error("Error deleting sale with saleId: {}", sale.getSaleId(), e);
            throw new RuntimeException("Error deleting sale", e);
        }
    }
}
