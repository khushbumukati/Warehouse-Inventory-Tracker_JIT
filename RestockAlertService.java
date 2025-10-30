package com.warehouse.service;

import com.warehouse.model.Product;
import java.time.LocalDateTime;

public class RestockAlertService implements AlertService {
    @Override
    public void onLowStock(Product product) {
        String msg = String.format("[%s] Restock Alert: Low stock for %s (id=%s) - only %d left!",
                LocalDateTime.now(), product.getName(), product.getId(), product.getQuantity());
        System.out.println(msg);
    }
}
