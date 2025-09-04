package org.example.catalog.exception;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String id) {
        super("Product not found with id: " + id);
    }
}
