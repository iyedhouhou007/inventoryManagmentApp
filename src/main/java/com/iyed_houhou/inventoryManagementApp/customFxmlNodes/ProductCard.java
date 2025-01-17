package com.iyed_houhou.inventoryManagementApp.customFxmlNodes;

import com.iyed_houhou.inventoryManagementApp.models.Product;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class ProductCard extends VBox {
    private final Product product;
    private ImageView productImageView;

    // Constructor
    public ProductCard(Product product) {
        this.product = product;

        // Set the alignment and spacing for the VBox
        this.setAlignment(Pos.CENTER);
        this.setSpacing(10);
        this.setMaxWidth(250);
        this.setMinWidth(150);
        this.getStyleClass().add("product-card"); // Add class for the entire card

        // Create the ImageView for product image if a valid image URL is provided
        if (product.getImgURL() != null && !product.getImgURL().isEmpty()) {
            this.productImageView = new ImageView(new Image(product.getImgURL()));
            this.productImageView.setFitHeight(150);
            this.productImageView.setFitWidth(150);
            this.productImageView.getStyleClass().add("product-image"); // Add class for
            this.productImageView.getStyleClass().add("product-image"); // Add class for the product image
            this.getChildren().add(productImageView);  // Add the image only if available
        }

        // Add ID label
        Label idLabel = new Label("ID: " + product.getProductId());

        // Add product name
        Text productName = new Text(product.getName());
        productName.setFont(Font.font(18));
        productName.setFill(Color.BLACK);
        productName.getStyleClass().add("product-name");

        // Add product price
        Text productPrice = new Text("DA" + product.getSalePrice());
        productPrice.setFont(Font.font(16));
        productPrice.setFill(Color.GREEN);
        productPrice.getStyleClass().add("product-price");

        // Add product supplier
        Text productSupplier = new Text(product.getSupplier().getName());
        productSupplier.setFont(Font.font(16));
        productSupplier.setFill(Color.BLUE);
        productSupplier.getStyleClass().add("product-supplier");

        // Add product quantity
        Text productQuantity = new Text("Quantity: " + product.getQuantity());
        productQuantity.setFont(Font.font(14));
        productQuantity.setFill(Color.DARKGRAY);

        // Add all elements back to the VBox
        this.getChildren().addAll(idLabel, productName, productPrice, productSupplier, productQuantity);
    }

    public void refreshUI() {
        // Clear all children to reset the UI
        this.getChildren().clear();

        // Rebuild the UI based on the updated product data
        if (product.getImgURL() != null && !product.getImgURL().isEmpty()) {
            if (this.productImageView == null) {
                this.productImageView = new ImageView();
                this.productImageView.setFitHeight(150);
                this.productImageView.setFitWidth(150);
                this.productImageView.getStyleClass().add("product-image");
            }
            this.productImageView.setImage(new Image(product.getImgURL()));
            this.getChildren().add(productImageView);
        }

        // Add ID label
        Label idLabel = new Label("ID: " + product.getProductId());

        // Add product name
        Text productName = new Text(product.getName());
        productName.setFont(Font.font(18));
        productName.setFill(Color.BLACK);
        productName.getStyleClass().add("product-name");

        // Add product price
        Text productPrice = new Text("DA" + product.getSalePrice());
        productPrice.setFont(Font.font(16));
        productPrice.setFill(Color.GREEN);
        productPrice.getStyleClass().add("product-price");

        // Add product supplier
        Text productSupplier = new Text(product.getSupplier().getName());
        productSupplier.setFont(Font.font(16));
        productSupplier.setFill(Color.BLUE);
        productSupplier.getStyleClass().add("product-supplier");

        // Add product quantity
        Text productQuantity = new Text("Quantity: " + product.getQuantity());
        productQuantity.setFont(Font.font(14));
        productQuantity.setFill(Color.DARKGRAY);

        // Add all elements back to the VBox
        this.getChildren().addAll(idLabel, productName, productPrice, productSupplier, productQuantity);
    }


    // Method to set a product image URL dynamically
    public void setProductImage(String imageUrl) {
        if (imageUrl != null && !imageUrl.isEmpty()) {
            if (this.productImageView == null) {
                this.productImageView = new ImageView();
                this.productImageView.setFitHeight(150);
                this.productImageView.setFitWidth(150);
                this.productImageView.getStyleClass().add("product-image");
                this.getChildren().add(1, productImageView); // Add the image at the top
            }
            this.productImageView.setImage(new Image(imageUrl));
        }
    }

    public Product getProduct() {
        return product;
    }
}
