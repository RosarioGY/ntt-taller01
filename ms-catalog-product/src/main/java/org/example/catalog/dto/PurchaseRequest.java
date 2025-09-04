package org.example.catalog.dto;

import lombok.Data;
import jakarta.validation.constraints.Positive;

@Data
public class PurchaseRequest {

    @Positive(message = "Quantity must be positive")
    private Integer quantity;
}
