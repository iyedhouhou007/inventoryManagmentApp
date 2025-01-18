package com.iyed_houhou.inventoryManagementApp.repositories;

import com.iyed_houhou.inventoryManagementApp.models.Product;
import com.iyed_houhou.inventoryManagementApp.models.Supplier;
import com.iyed_houhou.inventoryManagementApp.config.AppConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
    private static final Logger logger = LoggerFactory.getLogger(ProductDAO.class);
    private final Connection connection;
    private final SupplierDAO supplierDAO; // Dependency to manage suppliers

    public ProductDAO() {
        this.supplierDAO = new SupplierDAO();
        try {
            connection = DriverManager.getConnection(AppConfig.DB_URL);
            createProductTableIfNotExists();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to connect to the SQLite database", e);
        }
    }

    private void createProductTableIfNotExists() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS products (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " + // Auto-increment ID
                "barcode TEXT UNIQUE NOT NULL, " +
                "name TEXT NOT NULL, " +
                "salePrice REAL NOT NULL, " +
                "buyPrice REAL NOT NULL, " +
                "avgBuyPrice REAL, " +
                "quantity INTEGER NOT NULL, " +
                "supplierId INTEGER NOT NULL, " +
                "imgURL TEXT, " +
                "FOREIGN KEY (supplierId) REFERENCES suppliers(id)" +
                ")";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTableSQL);
            logger.info("Product table ensured to exist with auto-increment ID");
        } catch (SQLException e) {
            logger.error("Error creating product table", e);
        }
    }

    // Add a new product
    public int addProduct(Product product) {
        String insertSQL = "INSERT INTO products (barcode, name, salePrice, buyPrice, avgBuyPrice, quantity, supplierId, imgURL) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, product.getProductBarCode());
            pstmt.setString(2, product.getName());
            pstmt.setDouble(3, product.getSalePrice());
            pstmt.setDouble(4, product.getBuyPrice());
            pstmt.setDouble(5, product.getAvgBuyPrice());
            pstmt.setInt(6, product.getQuantity());
            pstmt.setInt(7, product.getSupplier().getSupplierId());
            pstmt.setString(8, product.getImgURL());

            pstmt.executeUpdate();

            // Retrieve the auto-generated ID
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1); // Return generated ID
                }
            }
        } catch (SQLException e) {
            logger.error("Error adding product", e);
        }
        return -1; // Return -1 if adding product fails
    }

    // Retrieve a product by its ID
    public Product getProductById(int productId) {
        String selectSQL = "SELECT * FROM products WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(selectSQL)) {
            pstmt.setInt(1, productId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapToProduct(rs);
            }
        } catch (SQLException e) {
            logger.error("Error retrieving product by ID", e);
        }
        return null;
    }

    // Get all products
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String selectSQL = "SELECT * FROM products";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(selectSQL)) {

            while (rs.next()) {
                products.add(mapToProduct(rs));
            }
        } catch (SQLException e) {
            logger.error("Error retrieving all products", e);
        }
        return products;
    }

    // Update a product
    public void updateProduct(Product product) {
        String updateSQL = "UPDATE products SET barcode = ?, name = ?, salePrice = ?, buyPrice = ?, avgBuyPrice = ?, " +
                "quantity = ?, supplierId = ?, imgURL = ? WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(updateSQL)) {
            pstmt.setString(1, product.getProductBarCode());
            pstmt.setString(2, product.getName());
            pstmt.setDouble(3, product.getSalePrice());
            pstmt.setDouble(4, product.getBuyPrice());
            pstmt.setDouble(5, product.getAvgBuyPrice());
            pstmt.setInt(6, product.getQuantity());
            pstmt.setInt(7, product.getSupplier().getSupplierId());
            pstmt.setString(8, product.getImgURL());
            pstmt.setInt(9, product.getProductId());
            pstmt.executeUpdate();
            logger.info("Product updated: {}", product.getName());
        } catch (SQLException e) {
            logger.error("Error updating product", e);
        }
    }

    // Delete a product
    public void deleteProduct(int productId) {
        String deleteSQL = "DELETE FROM products WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(deleteSQL)) {
            pstmt.setInt(1, productId);
            pstmt.executeUpdate();
            logger.info("Product deleted with ID: {}", productId);
        } catch (SQLException e) {
            logger.error("Error deleting product", e);
        }
    }

    // Map ResultSet to Product
    private Product mapToProduct(ResultSet rs) throws SQLException {
        Supplier supplier = supplierDAO.getSupplierById(rs.getInt("supplierId"));
        return new Product(
                rs.getInt("id"),
                rs.getString("barcode"),
                rs.getString("name"),
                rs.getDouble("salePrice"),
                rs.getInt("quantity"),
                rs.getDouble("buyPrice"),
                supplier
        );
    }

    // Find a product by its barcode
    public Product findByBarcode(String barcode) {
        String selectSQL = "SELECT * FROM products WHERE barcode = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(selectSQL)) {
            pstmt.setString(1, barcode);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapToProduct(rs);  // Map the result set to a Product object
            }
        } catch (SQLException e) {
            logger.error("Error retrieving product by barcode", e);
        }
        return null;  // Return null if no product is found
    }

}
