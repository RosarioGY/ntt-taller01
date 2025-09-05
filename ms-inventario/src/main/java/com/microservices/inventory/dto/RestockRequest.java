package com.microservices.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestockRequest {

    @NotBlank(message = "Product ID is required")
    private String productId;

    @Positive(message = "Quantity must be positive")
    private Integer quantity;

    private String reference;
}
