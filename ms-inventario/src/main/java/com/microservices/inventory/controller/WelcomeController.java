package com.microservices.inventory.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/")
@Tag(name = "General", description = "Endpoints generales del microservicio")
public class WelcomeController {

    @GetMapping
    @Operation(summary = "Información del microservicio",
               description = "Obtiene información general del microservicio y endpoints disponibles")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Información obtenida exitosamente")
    })
    public Map<String, Object> welcome() {
        Map<String, Object> response = new HashMap<>();
        response.put("service", "Inventory Microservice");
        response.put("version", "1.0.0");
        response.put("status", "UP");
        response.put("timestamp", LocalDateTime.now());

        Map<String, String> endpoints = new HashMap<>();
        endpoints.put("GET /inventory", "Consultar stock por productId");
        endpoints.put("POST /inventory/{productId}/restock", "Agregar stock");
        endpoints.put("POST /reservations", "Crear reserva");
        endpoints.put("POST /reservations/{id}/confirm", "Confirmar reserva");
        endpoints.put("POST /reservations/{id}/release", "Liberar reserva");

        response.put("endpoints", endpoints);
        return response;
    }

    @GetMapping("/health")
    @Operation(summary = "Estado del microservicio",
               description = "Verifica el estado de salud del microservicio")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Servicio funcionando correctamente")
    })
    public Map<String, Object> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("timestamp", LocalDateTime.now());
        response.put("service", "ms-inventario");
        return response;
    }
}
