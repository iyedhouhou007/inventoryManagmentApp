package com.iyed_houhou.inventoryManagementApp.DAOs;

import com.iyed_houhou.inventoryManagementApp.config.DatabaseConnectionManager;
import com.iyed_houhou.inventoryManagementApp.models.Product;
import com.iyed_houhou.inventoryManagementApp.models.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
    private static final Logger logger = LoggerFactory.getLogger(ProductDAO.class);
    private final SupplierDAO supplierDAO; // Dependency to manage suppliers
    private static ProductDAO instance;

    private ProductDAO() {
        this.supplierDAO = SupplierDAO.getInstance();
        createProductTableIfNotExists(); // Ensure the table exists
    }

    public static ProductDAO getInstance() {
        if (instance == null) {
            instance = new ProductDAO();
        }
        return instance;
    }

    // Ensure the product table exists
    private void createProductTableIfNotExists() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS products (" +
                "productId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "barcode TEXT UNIQUE NOT NULL, " +
                "name TEXT NOT NULL, " +
                "salePrice REAL NOT NULL, " +
                "buyPrice REAL NOT NULL, " +
                "avgBuyPrice REAL, " +
                "quantity INTEGER NOT NULL, " +
                "supplierId INTEGER NOT NULL, " +
                "imgURL TEXT, " +
                "FOREIGN KEY (supplierId) REFERENCES suppliers(supplierId)" +
                ")";

        try (Connection connection = DatabaseConnectionManager.getInstance().getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.execute(createTableSQL);
            logger.info("Product table ensured to exist");
        } catch (SQLException e) {
            logger.error("Error creating product table", e);
        }
    }

    // Add a new product
    public int addProduct(Product product) {
        String insertSQL = "INSERT INTO products (barcode, name, salePrice, buyPrice, avgBuyPrice, quantity, supplierId, imgURL) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnectionManager.getInstance().getConnection();
             PreparedStatement pstmt = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, product.getProductBarCode());
            pstmt.setString(2, product.getName());
            pstmt.setDouble(3, product.getSalePrice());
            pstmt.setDouble(4, product.getBuyPrice());
            pstmt.setDouble(5, product.getAvgBuyPrice());
            pstmt.setInt(6, product.getQuantity());
            pstmt.setInt(7, product.getSupplier().getSupplierId());
            pstmt.setString(8, product.getImgURL());

            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1); // Return the generated ID
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("Error adding product", e);
        }
        return -1; // Return -1 if adding product fails
    }

    // Retrieve a product by its ID
    public Product getProductById(int productId) {
        String selectSQL = "SELECT * FROM products WHERE productId = ?";
        try (Connection connection = DatabaseConnectionManager.getInstance().getConnection();
             PreparedStatement pstmt = connection.prepareStatement(selectSQL)) {

            pstmt.setInt(1, productId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapToProduct(rs);
                }
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

        try (Connection connection = DatabaseConnectionManager.getInstance().getConnection();
             Statement stmt = connection.createStatement();
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
                "quantity = ?, supplierId = ?, imgURL = ? WHERE productId = ?";

        try (Connection connection = DatabaseConnectionManager.getInstance().getConnection();
             PreparedStatement pstmt = connection.prepareStatement(updateSQL)) {

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
        String deleteSQL = "DELETE FROM products WHERE productId = ?";
        try (Connection connection = DatabaseConnectionManager.getInstance().getConnection();
             PreparedStatement pstmt = connection.prepareStatement(deleteSQL)) {

            pstmt.setInt(1, productId);
            pstmt.executeUpdate();
            logger.info("Product deleted with ID: {}", productId);
        } catch (SQLException e) {
            logger.error("Error deleting product", e);
        }
    }

    // Map a ResultSet to a Product object
    private Product mapToProduct(ResultSet rs) throws SQLException {
        Supplier supplier = supplierDAO.getSupplierById(rs.getInt("supplierId"));
        return new Product(
                rs.getInt("productId"),
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
        try (Connection connection = DatabaseConnectionManager.getInstance().getConnection();
             PreparedStatement pstmt = connection.prepareStatement(selectSQL)) {

            pstmt.setString(1, barcode);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapToProduct(rs);
                }
            }
        } catch (SQLException e) {
            logger.error("Error retrieving product by barcode", e);
        }
        return null;
    }
}
