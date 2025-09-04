package org.example.catalog.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.catalog.dto.ChangeStockRequest;
import org.example.catalog.dto.PurchaseRequest;
import org.example.catalog.dto.UpdateProductRequest;
import org.example.catalog.entity.Product;
import org.example.catalog.exception.NotFoundException;
import org.example.catalog.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;

    @GetMapping
    public List<Product> list() {
        return service.list();
    }

    @GetMapping("/{id}")
    public Product get(@PathVariable String id) {
        return service.list().stream().filter(p -> p.getId().equals(id))
                .findFirst().orElseThrow(() -> new NotFoundException(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Product create(@Valid @RequestBody Product body) {
        return service.create(body);
    }

    @PutMapping("/{id}")
    public Product update(@PathVariable String id, @Valid @RequestBody UpdateProductRequest body) {
        return service.update(id, body.getName(), body.getPrice());
    }

    @PatchMapping("/{id}/stock")
    public Product addStock(@PathVariable String id, @RequestBody ChangeStockRequest body) {
        return service.addStock(id, body.getAmount()); // amount > 0
    }

    @PostMapping("/{id}/purchase")
    public Product purchase(@PathVariable String id, @RequestBody PurchaseRequest body) {
        return service.purchase(id, body.getQuantity()); // quantity > 0
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        service.delete(id);
    }
}
