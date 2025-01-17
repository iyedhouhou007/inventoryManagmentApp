package com.iyed_houhou.inventoryManagementApp.models;

public class Product {
    private int productId;
    private String name;
    private double salePrice;
    private double buyPrice;
    private double avgBuyPrice;
    private int quantity;
    private Supplier supplier;
    private String imgURL;

    public Product(int productId, String name, double salePrice, int quantity, Supplier supplier) {
        this.productId = productId;
        this.name = name;
        this.salePrice = salePrice;
        this.quantity = quantity;
        this.supplier = supplier;
        this.avgBuyPrice = buyPrice;
    }

    public Product(int productId, String name, double salePrice, int quantity, double buyPrice, Supplier supplier) {
        this.productId = productId;
        this.name = name;
        this.salePrice = salePrice;
        this.buyPrice = buyPrice;
        this.quantity = quantity;
        this.supplier = supplier;
        this.avgBuyPrice = buyPrice;
    }

    public boolean isAvailable() {
        return this.quantity > 0;
    }

    public void applyStockAlert() {
        // Apply stock alert logic
        System.out.println("Alert: Low stock for product: " + this.name);
    }

    public int getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public void addQuantity(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null.");
        }

        if (product.getQuantity() <= 0) {
            throw new IllegalArgumentException("Product quantity must be greater than 0.");
        }

        int prevQuantity = getQuantity();
        int newQuantity = product.getQuantity();

        // Avoid division by zero
        if (prevQuantity + newQuantity > 0) {
            avgBuyPrice = ((product.getBuyPrice() * newQuantity) + (buyPrice * prevQuantity)) / (double) (prevQuantity + newQuantity);
        } else {
            avgBuyPrice = 0; // Fallback case (shouldn't occur if input validation is correct)
        }

        setQuantity(prevQuantity + newQuantity);
    }



    public double getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(double buyPrice) {
        this.buyPrice = buyPrice;
    }
}

