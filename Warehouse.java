package com.warehouse.core;

import com.warehouse.model.Product;
import com.warehouse.service.AlertService;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Warehouse {
    private final String id;
    private final Map<String, Product> inventory = new ConcurrentHashMap<>();
    private final List<AlertService> observers = Collections.synchronizedList(new ArrayList<>());

    public Warehouse(String id) {
        this.id = Objects.requireNonNull(id);
    }

    public String getId() { return id; }

    public void addObserver(AlertService observer) { observers.add(observer); }
    public void removeObserver(AlertService observer) { observers.remove(observer); }

    private void notifyIfLow(Product p) {
        if (p.getQuantity() < p.getReorderThreshold()) {
            synchronized (observers) {
                for (AlertService o : observers) {
                    try { o.onLowStock(p); } catch (Exception ex) { System.err.println("Observer failed: " + ex.getMessage()); }
                }
            }
        }
    }

    public void addProduct(Product product) {
        if (product == null) throw new IllegalArgumentException("Product cannot be null");
        inventory.put(product.getId(), product);
        notifyIfLow(product);
    }

    public Optional<Product> getProduct(String id) { return Optional.ofNullable(inventory.get(id)); }

    public void receiveShipment(String productId, int amount) {
        Product p = inventory.get(productId);
        if (p == null) throw new NoSuchElementException("Product id not found: " + productId);
        p.increaseQuantity(amount);
        notifyIfLow(p);
    }

    public void fulfillOrder(String productId, int amount) {
        Product p = inventory.get(productId);
        if (p == null) throw new NoSuchElementException("Product id not found: " + productId);
        p.decreaseQuantity(amount);
        notifyIfLow(p);
    }

    public Map<String, Product> snapshot() { return Collections.unmodifiableMap(inventory); }
}
