package com.microservices.inventory.service;

import com.microservices.inventory.domain.entity.Movement;
import com.microservices.inventory.domain.entity.Reservation;
import com.microservices.inventory.domain.entity.Stock;
import com.microservices.inventory.dto.ReservationRequest;
import com.microservices.inventory.dto.ReservationResponse;
import com.microservices.inventory.dto.StockResponse;
import com.microservices.inventory.exception.InsufficientStockException;
import com.microservices.inventory.exception.ReservationNotFoundException;
import com.microservices.inventory.exception.StockNotFoundException;
import com.microservices.inventory.repository.MovementRepository;
import com.microservices.inventory.repository.ReservationRepository;
import com.microservices.inventory.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final StockRepository stockRepository;
    private final MovementRepository movementRepository;
    private final ReservationRepository reservationRepository;
    private final EventPublisherService eventPublisher;

    @Transactional
    public void restockProduct(String productId, Integer quantity, String reference) {
        log.info("Restocking product {} with quantity {}", productId, quantity);

        try {
            Stock stock = stockRepository.findByProductId(productId)
                    .orElse(Stock.builder()
                            .productId(productId)
                            .onHand(0)
                            .reserved(0)
                            .version(0L)
                            .build());

            stock.addStock(quantity);
            stock = stockRepository.save(stock);

            // Record movement
            Movement movement = Movement.builder()
                    .productId(productId)
                    .type(Movement.MovementType.RESTOCK)
                    .quantity(quantity)
                    .externalReference(reference)
                    .timestamp(LocalDateTime.now())
                    .build();
            movementRepository.save(movement);

            // Publish event
            eventPublisher.publishStockIncreased(productId, quantity, stock.getOnHand());

        } catch (OptimisticLockingFailureException e) {
            log.warn("Optimistic locking failure for product {}, retrying...", productId);
            throw new RuntimeException("Concurrent modification detected, please retry");
        }
    }

    public StockResponse getStock(String productId) {
        Stock stock = stockRepository.findByProductId(productId)
                .orElseThrow(() -> new StockNotFoundException(productId));

        return StockResponse.builder()
                .productId(stock.getProductId())
                .onHand(stock.getOnHand())
                .reserved(stock.getReserved())
                .available(stock.getAvailable())
                .version(stock.getVersion())
                .build();
    }

    @Transactional
    public ReservationResponse createReservation(ReservationRequest request) {
        log.info("Creating reservation for order {}", request.getOrderId());

        try {
            // Validate stock availability for all items
            for (ReservationRequest.ReservationItemRequest item : request.getItems()) {
                Stock stock = stockRepository.findByProductId(item.getProductId())
                        .orElseThrow(() -> new StockNotFoundException(item.getProductId()));

                if (!stock.hasEnoughStock(item.getQuantity())) {
                    throw new InsufficientStockException(item.getProductId(),
                            item.getQuantity(), stock.getAvailable());
                }
            }

            // Create reservation
            Reservation reservation = Reservation.builder()
                    .orderId(request.getOrderId())
                    .items(request.getItems().stream()
                            .map(item -> Reservation.ReservationItem.builder()
                                    .productId(item.getProductId())
                                    .quantity(item.getQuantity())
                                    .build())
                            .collect(Collectors.toList()))
                    .status(Reservation.ReservationStatus.ACTIVE)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            reservation = reservationRepository.save(reservation);

            // Reserve stock for each item
            for (ReservationRequest.ReservationItemRequest item : request.getItems()) {
                Stock stock = stockRepository.findByProductId(item.getProductId())
                        .orElseThrow(() -> new StockNotFoundException(item.getProductId()));

                stock.reserveStock(item.getQuantity());
                stockRepository.save(stock);

                // Record movement
                Movement movement = Movement.builder()
                        .productId(item.getProductId())
                        .type(Movement.MovementType.RESERVATION)
                        .quantity(item.getQuantity())
                        .externalReference(reservation.getId())
                        .timestamp(LocalDateTime.now())
                        .build();
                movementRepository.save(movement);
            }

            // Publish event
            eventPublisher.publishReservationCreated(reservation.getId(), request.getOrderId());

            return mapToReservationResponse(reservation);

        } catch (OptimisticLockingFailureException e) {
            log.warn("Optimistic locking failure during reservation creation, retrying...");
            throw new RuntimeException("Concurrent modification detected, please retry");
        }
    }

    @Transactional
    public void confirmReservation(String reservationId) {
        log.info("Confirming reservation {}", reservationId);

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException(reservationId));

        if (reservation.getStatus() != Reservation.ReservationStatus.ACTIVE) {
            throw new IllegalStateException("Reservation is not in ACTIVE status");
        }

        try {
            // Confirm stock for each item
            for (Reservation.ReservationItem item : reservation.getItems()) {
                Stock stock = stockRepository.findByProductId(item.getProductId())
                        .orElseThrow(() -> new StockNotFoundException(item.getProductId()));

                stock.confirmReservation(item.getQuantity());
                stockRepository.save(stock);

                // Record movement
                Movement movement = Movement.builder()
                        .productId(item.getProductId())
                        .type(Movement.MovementType.CONFIRMATION)
                        .quantity(item.getQuantity())
                        .externalReference(reservationId)
                        .timestamp(LocalDateTime.now())
                        .build();
                movementRepository.save(movement);
            }

            // Update reservation status
            reservation.setStatus(Reservation.ReservationStatus.CONFIRMED);
            reservation.setUpdatedAt(LocalDateTime.now());
            reservationRepository.save(reservation);

            // Publish event
            eventPublisher.publishReservationConfirmed(reservationId, reservation.getOrderId());

        } catch (OptimisticLockingFailureException e) {
            log.warn("Optimistic locking failure during reservation confirmation, retrying...");
            throw new RuntimeException("Concurrent modification detected, please retry");
        }
    }

    @Transactional
    public void releaseReservation(String reservationId) {
        log.info("Releasing reservation {}", reservationId);

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException(reservationId));

        if (reservation.getStatus() != Reservation.ReservationStatus.ACTIVE) {
            throw new IllegalStateException("Reservation is not in ACTIVE status");
        }

        try {
            // Release stock for each item
            for (Reservation.ReservationItem item : reservation.getItems()) {
                Stock stock = stockRepository.findByProductId(item.getProductId())
                        .orElseThrow(() -> new StockNotFoundException(item.getProductId()));

                stock.releaseReservation(item.getQuantity());
                stockRepository.save(stock);

                // Record movement
                Movement movement = Movement.builder()
                        .productId(item.getProductId())
                        .type(Movement.MovementType.RELEASE)
                        .quantity(item.getQuantity())
                        .externalReference(reservationId)
                        .timestamp(LocalDateTime.now())
                        .build();
                movementRepository.save(movement);
            }

            // Update reservation status
            reservation.setStatus(Reservation.ReservationStatus.RELEASED);
            reservation.setUpdatedAt(LocalDateTime.now());
            reservationRepository.save(reservation);

            // Publish event
            eventPublisher.publishReservationReleased(reservationId, reservation.getOrderId());

        } catch (OptimisticLockingFailureException e) {
            log.warn("Optimistic locking failure during reservation release, retrying...");
            throw new RuntimeException("Concurrent modification detected, please retry");
        }
    }

    private ReservationResponse mapToReservationResponse(Reservation reservation) {
        return ReservationResponse.builder()
                .reservationId(reservation.getId())
                .orderId(reservation.getOrderId())
                .items(reservation.getItems().stream()
                        .map(item -> ReservationResponse.ReservationItemResponse.builder()
                                .productId(item.getProductId())
                                .quantity(item.getQuantity())
                                .build())
                        .collect(Collectors.toList()))
                .status(reservation.getStatus().name())
                .createdAt(reservation.getCreatedAt())
                .build();
    }
}
