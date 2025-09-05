package com.microservices.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockResponse {

    private String productId;
    private Integer onHand;
    private Integer reserved;
    private Integer available;
    private Long version;
}
