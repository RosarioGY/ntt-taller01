package com.microservices.inventory.exception;

public class StockNotFoundException extends RuntimeException {

    public StockNotFoundException(String productId) {
        super(String.format("Stock not found for product: %s", productId));
    }
}
