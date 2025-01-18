package com.iyed_houhou.inventoryManagementApp.customFxmlNodes;

import com.iyed_houhou.inventoryManagementApp.config.AppConfig;
import com.iyed_houhou.inventoryManagementApp.controllers.ProductCardDetailsController;
import com.iyed_houhou.inventoryManagementApp.models.Product;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProductCard extends VBox {
    private final Product product;
    private ImageView productImageView;
    private static final Logger logger = Logger.getLogger(ProductCard.class.getName());


    public ProductCard(Product product) {
        this.product = product;

        // Set the alignment and spacing for the VBox
        this.setAlignment(Pos.CENTER);
        this.setSpacing(10);
        this.setMaxWidth(250);
        this.setMinWidth(150);
        this.getStyleClass().add("product-card");

        // Add all UI elements (image, labels, etc.)
        if (product.getImgURL() != null && !product.getImgURL().isEmpty()) {
            this.productImageView = new ImageView(new Image(product.getImgURL()));
            this.productImageView.setFitHeight(150);
            this.productImageView.setFitWidth(150);
            this.productImageView.getStyleClass().add("product-image");
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


        // Add click event handler
        this.setOnMouseClicked(this::handleClick);
    }

    private void handleClick(MouseEvent event) {
        System.out.println("Product clicked: " + product.getName());
        showProductDetails();
    }


    private void showProductDetails() {
        try {
            // Load the FXML file containing the DialogPane
            FXMLLoader loader = new FXMLLoader(getClass().getResource(AppConfig.VIEW_PATH + "ProductCardDetailsView.fxml"));
            DialogPane dialogPane = loader.load();

            // Optionally, pass the product data to the controller
            ProductCardDetailsController controller = loader.getController();
            controller.setProduct(product);

            // Create a Dialog and set the DialogPane
            Dialog<Void> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Product Details");

            // Set the dialog's owner to the application's main stage
            dialog.initOwner(AppConfig.OWNER); // Replace `Main.getPrimaryStage()` with your stage accessor.
            dialog.initModality(Modality.APPLICATION_MODAL); // Make the dialog modal

            // Find the Close button and apply the custom CSS class
            Button closeButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.CLOSE);
            if (closeButton != null) {
                closeButton.getStyleClass().add("close-btn");
            }

            Button applyButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.APPLY);
            if (applyButton != null) {
                applyButton.getStyleClass().add("save-button");
            }
            dialog.showAndWait(); // Display the dialog and wait for user interaction

            refreshUI();
        } catch (IOException e) {
            // Log the error with a proper message and stack trace
            logger.log(Level.SEVERE, "Failed to load the ProductCardDetailsView.fxml file", e);
        }
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
