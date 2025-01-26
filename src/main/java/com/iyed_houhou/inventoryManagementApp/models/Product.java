package com.iyed_houhou.inventoryManagementApp.models;

public class Product {
    private int productId;  // Added ID field for the product
    private final String productBarCode;
    private String name;
    private double salePrice;
    private double buyPrice;
    private double avgBuyPrice;
    private int quantity;
    private Supplier supplier;
    private String imgURL;


    // Constructor with ID and barcode, and buyPrice
    public Product(String productBarCode, String name, double salePrice, int quantity, double buyPrice, Supplier supplier) {
        this.productBarCode = productBarCode;
        this.name = name;
        this.salePrice = salePrice;
        this.buyPrice = buyPrice;
        this.quantity = quantity;
        this.supplier = supplier;
        this.avgBuyPrice = buyPrice;
    }

    public Product(int id, String barcode, String name, double salePrice, int quantity, double buyPrice, Supplier supplier) {
        this.productId = id;
        this.productBarCode = barcode;
        this.name = name;
        this.salePrice = salePrice;
        this.buyPrice = buyPrice;
        this.quantity = quantity;
        this.supplier = supplier;
        this.avgBuyPrice = buyPrice;
    }

    // Getter and Setter for productId
    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }


    // Getter and Setter methods
    public String getProductBarCode() {
        return productBarCode;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public double getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(double buyPrice) {
        this.buyPrice = buyPrice;
    }

    public double getAvgBuyPrice() {
        return avgBuyPrice;
    }

    // Method to add product quantity and update avgBuyPrice
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
            avgBuyPrice = 0;
        }

        setQuantity(prevQuantity + newQuantity);
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", productBarCode='" + productBarCode + '\'' +
                ", name='" + name + '\'' +
                ", salePrice=" + salePrice +
                ", buyPrice=" + buyPrice +
                ", avgBuyPrice=" + avgBuyPrice +
                ", quantity=" + quantity +
                ", supplier=" + supplier +
                ", imgURL='" + imgURL + '\'' +
                '}';
    }

}
