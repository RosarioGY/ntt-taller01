package com.microservices.inventory.repository;

import com.microservices.inventory.domain.entity.Reservation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends MongoRepository<Reservation, String> {

    Optional<Reservation> findByOrderId(String orderId);

    List<Reservation> findByStatus(Reservation.ReservationStatus status);
}
