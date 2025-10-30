package com.warehouse;

import com.warehouse.core.Warehouse;
import com.warehouse.core.WarehouseManager;
import com.warehouse.model.Product;
import com.warehouse.service.RestockAlertService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        WarehouseManager manager = new WarehouseManager("inventory.txt");
        Warehouse warehouse = manager.getWarehouse("default");
        warehouse.addObserver(new RestockAlertService());

        Product laptop = new Product("P1001", "Laptop", 0, 5);
        warehouse.addProduct(laptop);

        System.out.println("Receiving shipment of 10 Laptops...");
        warehouse.receiveShipment("P1001", 10);
        System.out.println("Fulfilling 6 orders...");
        warehouse.fulfillOrder("P1001", 6);

        try {
            warehouse.fulfillOrder("UNKNOWN", 1);
        } catch (Exception e) {
            System.err.println("Handled error: " + e.getMessage());
        }

        System.out.println("Starting multithreaded simulation...");
        ExecutorService ex = Executors.newFixedThreadPool(3);
        Runnable task1 = () -> { try { warehouse.receiveShipment("P1001", 5); } catch (Exception e) { System.err.println(e.getMessage()); } };
        Runnable task2 = () -> { try { warehouse.fulfillOrder("P1001", 3); } catch (Exception e) { System.err.println(e.getMessage()); } };
        Runnable task3 = () -> { try { warehouse.fulfillOrder("P1001", 4); } catch (Exception e) { System.err.println(e.getMessage()); } };

        ex.submit(task1);
        ex.submit(task2);
        ex.submit(task3);

        ex.shutdown();
        ex.awaitTermination(5, TimeUnit.SECONDS);

        System.out.println("Final product state: " + warehouse.getProduct("P1001").orElseThrow());

        manager.saveAll();
        System.out.println("Inventory saved to inventory.txt");
    }
}
