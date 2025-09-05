package com.microservices.inventory.controller;

import com.microservices.inventory.dto.RestockRequest;
import com.microservices.inventory.dto.StockResponse;
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
@RequestMapping("/inventory")
@RequiredArgsConstructor
@Tag(name = "Inventory", description = "API para gestión de inventario y stock")
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping("/{productId}/restock")
    @Operation(summary = "Agregar stock a un producto",
               description = "Incrementa el stock disponible de un producto específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Stock agregado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Void> restockProduct(
            @Parameter(description = "ID del producto", required = true)
            @PathVariable String productId,
            @Parameter(description = "Información de reabastecimiento", required = true)
            @Valid @RequestBody RestockRequest request) {

        inventoryService.restockProduct(productId, request.getQuantity(), request.getReference());
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @Operation(summary = "Consultar stock de un producto",
               description = "Obtiene información detallada del stock de un producto")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Stock consultado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<StockResponse> getStock(
            @Parameter(description = "ID del producto", required = true)
            @RequestParam String productId) {
        StockResponse stock = inventoryService.getStock(productId);
        return ResponseEntity.ok(stock);
    }
}
