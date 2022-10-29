package com.nttdata.bootcamp.cardservice.infrastructure;

import com.nttdata.bootcamp.cardservice.model.CardMovement;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CardMovementRepository extends ReactiveMongoRepository<CardMovement, String> {
}
