package com.microservices.inventory.exception;

public class ReservationNotFoundException extends RuntimeException {

    public ReservationNotFoundException(String reservationId) {
        super(String.format("Reservation not found: %s", reservationId));
    }
}
