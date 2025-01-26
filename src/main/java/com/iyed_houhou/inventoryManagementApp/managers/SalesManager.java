package com.iyed_houhou.inventoryManagementApp.managers;

import com.iyed_houhou.inventoryManagementApp.models.Sale;
import com.iyed_houhou.inventoryManagementApp.services.SalesService;

import java.util.List;

public class SalesManager {

    private static SalesManager instance;
    private final SalesService saleService;
    private final List<Sale> salesList;


    public SalesManager() {
        saleService = new SalesService();
        salesList = saleService.getAllSalesFromDB();  // Load initial list from DB
    }

    public static SalesManager getInstance() {
        if (instance == null) {
            instance = new SalesManager();
        }
        return instance;
    }

    // Adds a sale to the list and the database
    public void addSale(Sale sale) {
        // Add the sale to the local list
        salesList.add(sale);

        // Add the sale to the database using SaleService
        saleService.addSale(sale);
    }

    public void deleteSale(Sale sale){
        saleService.deleteSale(sale);
    }
}
