package com.iyed_houhou.inventoryManagementApp.controllers;

import javafx.fxml.FXML;

public class DashboardPageController extends BasePageController {


    @FXML
    private void openDashboardPage() {
        loadPage("DashboardView.fxml");
    }

    @FXML
    private void openProductsPage() {
        loadPage("ProductsPageView.fxml");
    }

    @FXML
    private void openReportsPage() {
        loadPage("ReportsPageView.fxml");
    }

    @FXML
    private void openSettingsPage() {
        loadPage("SettingsPageView.fxml");
    }

    @FXML
    private void openAddSalePage() {
        loadPage("AddSalePageView.fxml");
    }

    @FXML
    private void openSuppliersPage() {
        loadPage("SuppliersPageView.fxml");
    }
}
