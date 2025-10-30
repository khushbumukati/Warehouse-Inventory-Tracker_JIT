package com.warehouse.persistence;

import com.warehouse.model.Product;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class InventoryPersistence {
    private final Path file;

    public InventoryPersistence(String filePath) {
        this.file = Path.of(filePath);
    }

    public void save(Map<String, Product> products) {
        try (BufferedWriter writer = Files.newBufferedWriter(file)) {
            for (Product p : products.values()) {
                writer.write(String.join("|", p.getId(), p.getName(), String.valueOf(p.getQuantity()), String.valueOf(p.getReorderThreshold())));
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Failed to save inventory: " + e.getMessage());
        }
    }

    public Map<String, Product> load() {
        Map<String, Product> map = new HashMap<>();
        if (!Files.exists(file)) return map;
        try (BufferedReader reader = Files.newBufferedReader(file)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|", -1);
                if (parts.length < 4) continue;
                String id = parts[0];
                String name = parts[1];
                int qty = Integer.parseInt(parts[2]);
                int th = Integer.parseInt(parts[3]);
                map.put(id, new Product(id, name, qty, th));
            }
        } catch (IOException e) {
            System.err.println("Failed to load inventory: " + e.getMessage());
        }
        return map;
    }
}
