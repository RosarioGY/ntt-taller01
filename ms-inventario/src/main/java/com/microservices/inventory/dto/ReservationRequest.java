package com.microservices.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationRequest {

    @NotBlank(message = "Order ID is required")
    private String orderId;

    @NotEmpty(message = "Items list cannot be empty")
    @Valid
    private List<ReservationItemRequest> items;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReservationItemRequest {

        @NotBlank(message = "Product ID is required")
        private String productId;

        @Positive(message = "Quantity must be positive")
        private Integer quantity;
    }
}
