package com.microservices.inventory.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "reservations")
public class Reservation {

    @Id
    private String id;

    private String orderId;

    private List<ReservationItem> items;

    private ReservationStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public enum ReservationStatus {
        ACTIVE,
        CONFIRMED,
        RELEASED
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReservationItem {
        private String productId;
        private Integer quantity;
    }
}
