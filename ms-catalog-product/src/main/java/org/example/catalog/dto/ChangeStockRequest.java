package org.example.catalog.dto;

import lombok.Data;
import javax.validation.constraints.Positive;

@Data
public class ChangeStockRequest {

    @Positive(message = "Amount must be positive")
    private Integer amount;
}
