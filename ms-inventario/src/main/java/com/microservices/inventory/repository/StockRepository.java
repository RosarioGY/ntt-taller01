package com.microservices.inventory.repository;

import com.microservices.inventory.domain.entity.Stock;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StockRepository extends MongoRepository<Stock, String> {

    Optional<Stock> findByProductId(String productId);

    boolean existsByProductId(String productId);
}
