package org.example.catalog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.catalog.dto.ChangeStockRequest;
import org.example.catalog.dto.PurchaseRequest;
import org.example.catalog.dto.UpdateProductRequest;
import org.example.catalog.entity.Product;
import org.example.catalog.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
@Testcontainers
class ProductControllerIntegrationTest {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:6.0")
            .withExposedPorts(27017);

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.host", mongoDBContainer::getHost);
        registry.add("spring.data.mongodb.port", mongoDBContainer::getFirstMappedPort);
        registry.add("spring.data.mongodb.database", () -> "testdb");
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
    }

    @Test
    void shouldCreateProduct() throws Exception {
        Product product = new Product("Test Product", BigDecimal.valueOf(99.99), 10);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test Product"))
                .andExpect(jsonPath("$.price").value(99.99))
                .andExpect(jsonPath("$.stock").value(10));
    }

    @Test
    void shouldGetProductById() throws Exception {
        Product savedProduct = productRepository.save(new Product("Test Product", BigDecimal.valueOf(99.99), 10));

        mockMvc.perform(get("/products/{id}", savedProduct.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedProduct.getId()))
                .andExpect(jsonPath("$.name").value("Test Product"));
    }

    @Test
    void shouldUpdateProduct() throws Exception {
        Product savedProduct = productRepository.save(new Product("Test Product", BigDecimal.valueOf(99.99), 10));
        UpdateProductRequest updateRequest = new UpdateProductRequest();
        updateRequest.setName("Updated Product");
        updateRequest.setPrice(BigDecimal.valueOf(149.99));

        mockMvc.perform(put("/products/{id}", savedProduct.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Product"))
                .andExpect(jsonPath("$.price").value(149.99));
    }

    @Test
    void shouldAddStock() throws Exception {
        Product savedProduct = productRepository.save(new Product("Test Product", BigDecimal.valueOf(99.99), 10));
        ChangeStockRequest stockRequest = new ChangeStockRequest();
        stockRequest.setAmount(5);

        mockMvc.perform(patch("/products/{id}/stock", savedProduct.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(stockRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stock").value(15));
    }

    @Test
    void shouldPurchaseProduct() throws Exception {
        Product savedProduct = productRepository.save(new Product("Test Product", BigDecimal.valueOf(99.99), 10));
        PurchaseRequest purchaseRequest = new PurchaseRequest();
        purchaseRequest.setQuantity(3);

        mockMvc.perform(post("/products/{id}/purchase", savedProduct.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(purchaseRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stock").value(7));
    }

    @Test
    void shouldDeleteProduct() throws Exception {
        Product savedProduct = productRepository.save(new Product("Test Product", BigDecimal.valueOf(99.99), 10));

        mockMvc.perform(delete("/products/{id}", savedProduct.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/products/{id}", savedProduct.getId()))
                .andExpect(status().isNotFound());
    }
}
