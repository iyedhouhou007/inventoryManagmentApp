package com.iyed_houhou.inventoryManagementApp.services;

import com.iyed_houhou.inventoryManagementApp.DAOs.SaleItemDAO;
import com.iyed_houhou.inventoryManagementApp.models.Sale;
import com.iyed_houhou.inventoryManagementApp.DAOs.SalesDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SalesService {

    private static final Logger logger = LoggerFactory.getLogger(SalesService.class);
    private final SalesDAO salesDAO; // Dependency to manage database operations

    public SalesService() {
        this.salesDAO = SalesDAO.getInstance();
    }

    public List<Sale> getAllSalesFromDB() {
        return salesDAO.getAllSalesFromDB();
    }

    public void addSale(Sale sale) {
        if (sale.getSaleItems().isEmpty()) {
            logger.error("Sale does not contain any items.");
            throw new IllegalArgumentException("Sale must contain at least one item.");
        }

        int saleId = salesDAO.addSale(sale);
        if (saleId == -1) {
            logger.error("Failed to add sale to the database.");
            throw new RuntimeException("Failed to add sale.");
        }
        sale.setSaleId(saleId);
        sale.setSaleItemId();
    }

    public void deleteSale(Sale sale) {
        boolean deleted = salesDAO.deleteSale(sale);
        if (!deleted) {
            logger.error("Failed to delete sale with ID: {}", sale.getSaleId());
            throw new RuntimeException("Failed to delete sale.");
        }
    }



}
