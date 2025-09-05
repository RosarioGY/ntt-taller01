package com.microservices.inventory.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "stocks")
public class Stock {

    @Id
    private String id;

    private String productId;

    private Integer onHand;

    private Integer reserved;

    @Version
    private Long version;

    public Integer getAvailable() {
        return onHand - reserved;
    }

    public boolean hasEnoughStock(Integer quantity) {
        return getAvailable() >= quantity;
    }

    public void reserveStock(Integer quantity) {
        if (!hasEnoughStock(quantity)) {
            throw new IllegalArgumentException("Insufficient stock available");
        }
        this.reserved += quantity;
    }

    public void confirmReservation(Integer quantity) {
        if (this.reserved < quantity) {
            throw new IllegalArgumentException("Cannot confirm more than reserved");
        }
        this.reserved -= quantity;
        this.onHand -= quantity;
    }

    public void releaseReservation(Integer quantity) {
        if (this.reserved < quantity) {
            throw new IllegalArgumentException("Cannot release more than reserved");
        }
        this.reserved -= quantity;
    }

    public void addStock(Integer quantity) {
        this.onHand += quantity;
    }
}
