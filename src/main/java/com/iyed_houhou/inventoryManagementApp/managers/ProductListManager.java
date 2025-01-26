package com.iyed_houhou.inventoryManagementApp.managers;

import com.iyed_houhou.inventoryManagementApp.models.Product;
import com.iyed_houhou.inventoryManagementApp.services.ProductService;

import java.util.List;

public class ProductListManager {

    private static ProductListManager instance;
    private final ProductService productService;
    private List<Product> productList;

    private ProductListManager() {
        productService = new ProductService();
        productList = productService.getAllProductsInDB();  // Load initial list from DB
    }

    public static ProductListManager getInstance() {
        if (instance == null) {
            instance = new ProductListManager();
        }
        return instance;
    }

    // Fetches all products from the database through ProductService
    public List<Product> getProductList() {
        return productList;  // Return the local product list managed by this class
    }

    // Adds a product to the list and the database
    public void addProduct(Product product) {
        // Add the product to the local list
        productList.add(product);

        // Add the product to the database using ProductService
        productService.addProduct(product);
    }

    // Removes a product from the list and the database
    public void removeProduct(Product product) {
        // Remove the product from the local list
        productList.remove(product);

        // Remove the product from the database using ProductService
        productService.deleteProduct(product.getProductId());
    }

    // Updates a product in the list and in the database
    public void updateProduct(Product product) {
        // Update the product in the local list
        int index = getProductIndex(product.getProductId());
        if (index != -1) {
            productList.set(index, product);
        }

        // Update the product in the database using ProductService
        productService.updateProduct(product);
    }

    // Searches for a product by barcode in the local list
    public Product getProductByBarcode(String barcode) {
        for (Product p : productList) {
            if (p.getProductBarCode().equals(barcode)) {
                return p;  // Return the product if found
            }
        }
        return null;  // Return null if product not found
    }

    public Product getProductById(int productId){
        return productService.getProductById(productId);
    }

    // Helper method to find the index of a product by its ID
    private int getProductIndex(int productId) {
        for (int i = 0; i < productList.size(); i++) {
            if (productList.get(i).getProductId() == (productId)) {
                return i;
            }
        }
        return -1;  // Return -1 if product is not found
    }

    public void refreshProductsList() {
        //implement it
        productList = productService.refreshProductsList();
    }

}
