package com.iyed_houhou.inventoryManagementApp.services;

import com.iyed_houhou.inventoryManagementApp.DAOs.ProductDAO;
import com.iyed_houhou.inventoryManagementApp.models.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);
    private final ProductDAO productDAO; // Dependency to manage database operations

    public ProductService() {
        this.productDAO = ProductDAO.getInstance();
    }

    public List<Product> getAllProductsInDB(){
        return productDAO.getAllProducts();
    }
    // Add a new product
    public void addProduct(Product product) {
        // Business logic before adding the product (e.g., validation)
        if (product.getQuantity() < 0) {
            throw new IllegalArgumentException("Product quantity must be greater than 0.");
        }

        int productId = productDAO.addProduct(product);
        if (productId != -1) {
            product.setProductId(productId); // Set the generated ID
            logger.info("Product added: {}", product.getName());
        }
    }

    // Get product by ID
    public Product getProductById(int productId) {
        return productDAO.getProductById(productId);
    }

    // Update a product
    public void updateProduct(Product product) {
        productDAO.updateProduct(product);
    }

    // Find a product by its barcode
    public Product findProductByBarcode(String barcode) {
        return productDAO.findByBarcode(barcode);
    }

    // Delete a product
    public void deleteProduct(int productId) {
        productDAO.deleteProduct(productId);
    }

    public List<Product> refreshProductsList() {
        return getAllProductsInDB();
    }
}
