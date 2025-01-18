package com.iyed_houhou.inventoryManagementApp.repositories;

import com.iyed_houhou.inventoryManagementApp.models.Supplier;
import com.iyed_houhou.inventoryManagementApp.config.AppConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SupplierDAO {

    private static final Logger logger = LoggerFactory.getLogger(SupplierDAO.class);
    private final Connection connection;

    // Constructor to initialize the connection
    public SupplierDAO() {
        try {
            connection = DriverManager.getConnection(AppConfig.DB_URL);
            createSupplierTableIfNotExists();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to connect to the SQLite database", e);
        }
    }

    // Create the supplier table if it does not exist
    private void createSupplierTableIfNotExists() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS suppliers (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " + // Auto-increment ID
                "name TEXT NOT NULL, " +
                "contact TEXT NOT NULL" +
                ")";

        String addDefaultSupplierSQL = "INSERT INTO suppliers (name, contact) " +
                "SELECT 'default', 'contact' " +
                "WHERE NOT EXISTS (SELECT 1 FROM suppliers WHERE name = 'default' AND contact = 'contact')";

        try (Statement stmt = connection.createStatement()) {
            // Ensure the table exists
            stmt.execute(createTableSQL);
            logger.info("Supplier table ensured to exist with auto-increment ID");

            // Add the default supplier if it does not exist
            int rowsInserted = stmt.executeUpdate(addDefaultSupplierSQL);
            if (rowsInserted > 0) {
                logger.info("Default supplier added to the suppliers table.");
            } else {
                logger.info("Default supplier already exists in the suppliers table.");
            }
        } catch (SQLException e) {
            logger.error("Error creating supplier table or inserting default supplier", e);
        }
    }


    // Add a new supplier
    public int addSupplier(Supplier supplier) {
        String insertSQL = "INSERT INTO suppliers (name, contact) VALUES (?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, supplier.getName());
            pstmt.setString(2, supplier.getContact());

            pstmt.executeUpdate();

            // Retrieve the auto-generated ID
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1); // Return generated ID
                }
            }
        } catch (SQLException e) {
            logger.error("Error adding supplier", e);
        }
        return -1; // Return -1 if adding supplier fails
    }

    // Retrieve a supplier by their ID
    public Supplier getSupplierById(int supplierId) {
        String selectSQL = "SELECT * FROM suppliers WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(selectSQL)) {
            pstmt.setInt(1, supplierId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapToSupplier(rs);
            }
        } catch (SQLException e) {
            logger.error("Error retrieving supplier by ID", e);
        }
        return null;
    }

    // Update a supplier
    public void updateSupplier(Supplier supplier) {
        String updateSQL = "UPDATE suppliers SET name = ?, contact = ? WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(updateSQL)) {
            pstmt.setString(1, supplier.getName());
            pstmt.setString(2, supplier.getContact());
            pstmt.setInt(3, supplier.getSupplierId());
            pstmt.executeUpdate();
            logger.info("Supplier updated: {}", supplier.getName());
        } catch (SQLException e) {
            logger.error("Error updating supplier", e);
        }
    }

    // Delete a supplier
    public void deleteSupplier(int supplierId) {
        String deleteSQL = "DELETE FROM suppliers WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(deleteSQL)) {
            pstmt.setInt(1, supplierId);
            pstmt.executeUpdate();
            logger.info("Supplier deleted with ID: {}", supplierId);
        } catch (SQLException e) {
            logger.error("Error deleting supplier", e);
        }
    }

    // Map ResultSet to Supplier object
    private Supplier mapToSupplier(ResultSet rs) throws SQLException {
        return new Supplier(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("contact")
        );
    }

    // Get all suppliers from the database
    public List<Supplier> getAllSuppliersInDB() {
        List<Supplier> suppliers = new ArrayList<>();
        String selectSQL = "SELECT * FROM suppliers";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(selectSQL)) {

            while (rs.next()) {
                suppliers.add(mapToSupplier(rs));
            }

        } catch (SQLException e) {
            logger.error("Error retrieving all suppliers", e);
        }

        return suppliers;
    }
}
