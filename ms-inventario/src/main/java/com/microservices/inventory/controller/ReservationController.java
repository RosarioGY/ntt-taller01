package com.microservices.inventory.controller;

import com.microservices.inventory.dto.ReservationRequest;
import com.microservices.inventory.dto.ReservationResponse;
import com.microservices.inventory.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
@Tag(name = "Reservations", description = "API para gestión de reservas de inventario")
public class ReservationController {

    private final InventoryService inventoryService;

    @PostMapping
    @Operation(summary = "Crear una nueva reserva",
               description = "Crea una reserva de productos para una orden específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Reserva creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "409", description = "Stock insuficiente para la reserva"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ReservationResponse> createReservation(
            @Parameter(description = "Información de la reserva", required = true)
            @Valid @RequestBody ReservationRequest request) {

        try {
            ReservationResponse reservation = inventoryService.createReservation(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(reservation);
        } catch (Exception e) {
            // Return 409 Conflict if insufficient stock
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PostMapping("/{reservationId}/confirm")
    @Operation(summary = "Confirmar una reserva",
               description = "Confirma una reserva activa, descontando definitivamente el stock")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reserva confirmada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Reserva no encontrada"),
        @ApiResponse(responseCode = "400", description = "La reserva no está en estado válido para confirmar"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Void> confirmReservation(
            @Parameter(description = "ID de la reserva", required = true)
            @PathVariable String reservationId) {
        inventoryService.confirmReservation(reservationId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{reservationId}/release")
    @Operation(summary = "Liberar una reserva",
               description = "Libera una reserva activa, devolviendo el stock al inventario disponible")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reserva liberada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Reserva no encontrada"),
        @ApiResponse(responseCode = "400", description = "La reserva no está en estado válido para liberar"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Void> releaseReservation(
            @Parameter(description = "ID de la reserva", required = true)
            @PathVariable String reservationId) {
        inventoryService.releaseReservation(reservationId);
        return ResponseEntity.ok().build();
    }
}
