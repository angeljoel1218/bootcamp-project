package com.nttdata.bootcamp.cardservice.infrastructure;

import com.nttdata.bootcamp.cardservice.model.CardMovement;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

/**
 *
 * @since 2022
 */
public interface CardMovementRepository extends ReactiveMongoRepository<CardMovement, String> {
    Flux<CardMovement> findByCardId(String cardId);

    Flux<CardMovement> findByCardIdOrderByOperationDateDesc(String cardId);
}
