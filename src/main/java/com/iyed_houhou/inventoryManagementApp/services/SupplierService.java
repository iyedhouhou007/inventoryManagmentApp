package com.iyed_houhou.inventoryManagementApp.services;

import com.iyed_houhou.inventoryManagementApp.models.Supplier;
import com.iyed_houhou.inventoryManagementApp.repositories.SupplierDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SupplierService {

    private static final Logger logger = LoggerFactory.getLogger(SupplierService.class);
    private final SupplierDAO supplierDAO; // Dependency to manage database operations

    // Constructor to initialize the SupplierDAO
    public SupplierService() {
        this.supplierDAO = new SupplierDAO();
    }

    // Add a new supplier
    public void addSupplier(Supplier supplier) {
        // Business logic before adding the supplier (e.g., validation)
        if (supplier.getName().isEmpty() || supplier.getContact().isEmpty()) {
            throw new IllegalArgumentException("Supplier name and contact must not be empty.");
        }

        int supplierId = supplierDAO.addSupplier(supplier);
        if (supplierId != -1) {
            supplier.setSupplierId(supplierId); // Set the generated ID
            logger.info("Supplier added: {}", supplier.getName());
        }
    }

    // Get supplier by ID
    public Supplier getSupplierById(int supplierId) {
        return supplierDAO.getSupplierById(supplierId);
    }

    // Update a supplier
    public void updateSupplier(Supplier supplier) {
        supplierDAO.updateSupplier(supplier);
    }

    // Delete a supplier
    public void deleteSupplier(int supplierId) {
        supplierDAO.deleteSupplier(supplierId);
    }

    public List<Supplier> getAllSuppliersInDB() {
        return supplierDAO.getAllSuppliersInDB();
    }
}
