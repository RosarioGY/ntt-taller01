package com.microservices.inventory.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "movements")
public class Movement {

    @Id
    private String id;

    private String productId;

    private MovementType type;

    private Integer quantity;

    private String externalReference;

    private LocalDateTime timestamp;

    public enum MovementType {
        RESTOCK,
        ADJUSTMENT,
        RESERVATION,
        CONFIRMATION,
        RELEASE
    }
}
