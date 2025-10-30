package com.warehouse.model;

import java.util.Objects;

public class Product {
    private final String id;
    private final String name;
    private int quantity;
    private final int reorderThreshold;

    public Product(String id, String name, int quantity, int reorderThreshold) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("Product id cannot be null or blank");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Product name cannot be null or blank");
        if (quantity < 0) throw new IllegalArgumentException("Quantity cannot be negative");
        if (reorderThreshold < 0) throw new IllegalArgumentException("Reorder threshold cannot be negative");
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.reorderThreshold = reorderThreshold;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public synchronized int getQuantity() { return quantity; }

    public synchronized void increaseQuantity(int amount) {
        if (amount <= 0) throw new IllegalArgumentException("Increase amount must be positive");
        this.quantity += amount;
    }

    public synchronized void decreaseQuantity(int amount) {
        if (amount <= 0) throw new IllegalArgumentException("Decrease amount must be positive");
        if (amount > this.quantity) throw new IllegalArgumentException("Insufficient stock: requested " + amount + " but available " + this.quantity);
        this.quantity -= amount;
    }

    public int getReorderThreshold() { return reorderThreshold; }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", quantity=" + quantity +
                ", reorderThreshold=" + reorderThreshold +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
