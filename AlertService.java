package com.warehouse.service;

import com.warehouse.model.Product;

public interface AlertService {
    void onLowStock(Product product);
}
