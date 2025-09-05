package com.microservices.inventory.repository;

import com.microservices.inventory.domain.entity.Movement;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovementRepository extends MongoRepository<Movement, String> {

    List<Movement> findByProductIdOrderByTimestampDesc(String productId);

    List<Movement> findByExternalReference(String externalReference);
}
