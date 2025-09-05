package org.example.catalog.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

@Data
public class UpdateProductRequest {

    @NotBlank(message = "Product name is required")
    private String name;

    @NotNull(message = "Product price is required")
    @PositiveOrZero(message = "Price must be positive or zero")
    private BigDecimal price;
}
