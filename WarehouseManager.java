package com.warehouse.core;

import com.warehouse.model.Product;
import com.warehouse.persistence.InventoryPersistence;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WarehouseManager {
    private final Map<String, Warehouse> warehouses = new ConcurrentHashMap<>();
    private final InventoryPersistence persistence;

    public WarehouseManager(String persistenceFile) {
        this.persistence = new InventoryPersistence(persistenceFile);
        Map<String, Product> loaded = persistence.load();
        Warehouse defaultWh = new Warehouse("default");
        for (Product p : loaded.values()) defaultWh.addProduct(p);
        warehouses.put(defaultWh.getId(), defaultWh);
    }

    public Warehouse createWarehouse(String id) {
        Warehouse w = new Warehouse(id);
        warehouses.put(id, w);
        return w;
    }

    public Warehouse getWarehouse(String id) { return warehouses.get(id); }

    public void saveAll() {
        Warehouse w = warehouses.get("default");
        if (w != null) persistence.save(w.snapshot());
    }
}
