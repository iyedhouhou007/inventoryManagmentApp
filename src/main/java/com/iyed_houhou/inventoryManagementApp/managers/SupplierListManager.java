package com.iyed_houhou.inventoryManagementApp.managers;

import com.iyed_houhou.inventoryManagementApp.models.Supplier;
import com.iyed_houhou.inventoryManagementApp.services.SupplierService;

import java.util.List;

public class SupplierListManager {

    private static SupplierListManager instance;
    private final SupplierService supplierService;
    private final List<Supplier> supplierList;

    // Private constructor to enforce Singleton pattern
    private SupplierListManager() {
        supplierService = new SupplierService();
        supplierList = supplierService.getAllSuppliersInDB();  // Load initial list from DB
    }

    // Get the single instance of SupplierListManager (Singleton pattern)
    public static SupplierListManager getInstance() {
        if (instance == null) {
            instance = new SupplierListManager();
        }
        return instance;
    }

    // Fetches all suppliers from the database through SupplierService
    public List<Supplier> getSupplierList() {
        return supplierList;  // Return the local supplier list managed by this class
    }

    // Adds a supplier to the list and the database
    public void addSupplier(Supplier supplier) {
        // Add the supplier to the local list
        supplierList.add(supplier);

        // Add the supplier to the database using SupplierService
        supplierService.addSupplier(supplier);
    }

    // Removes a supplier from the list and the database
    public void removeSupplier(Supplier supplier) {
        // Remove the supplier from the local list
        supplierList.remove(supplier);
        ProductListManager pSM = ProductListManager.getInstance();
        pSM.refreshProductsList();

        // Remove the supplier from the database using SupplierService
        supplierService.deleteSupplier(supplier.getSupplierId());
    }

    // Updates a supplier in the list and in the database
    public void updateSupplier(Supplier supplier) {
        // Update the supplier in the local list
        int index = findSupplierIndex(supplier.getSupplierId());
        if (index != -1) {
            supplierList.set(index, supplier);
        }

        // Update the supplier in the database using SupplierService
        supplierService.updateSupplier(supplier);
    }



    // Helper method to find the index of a supplier by its ID
    private int findSupplierIndex(int supplierId) {
        for (int i = 0; i < supplierList.size(); i++) {
            if (supplierList.get(i).getSupplierId() == supplierId) {
                return i;
            }
        }
        return -1;  // Return -1 if supplier is not found
    }
}
