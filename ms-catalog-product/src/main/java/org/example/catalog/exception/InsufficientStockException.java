package org.example.catalog.exception;

public class InsufficientStockException extends RuntimeException {

    public InsufficientStockException(String productId, Integer requestedQuantity, Integer availableStock) {
        super(String.format("Insufficient stock for product %s. Requested: %d, Available: %d",
              productId, requestedQuantity, availableStock));
    }
}
