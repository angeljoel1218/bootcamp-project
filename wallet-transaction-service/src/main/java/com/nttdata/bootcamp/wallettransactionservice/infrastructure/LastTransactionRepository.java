package com.nttdata.bootcamp.wallettransactionservice.infrastructure;

import com.nttdata.bootcamp.wallettransactionservice.model.LastTransaction;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class LastTransactionRepository {
    private final ReactiveRedisOperations<String, LastTransaction> reactiveRedisOperations;

    public Mono<LastTransaction> findById(String id) {
        return this.reactiveRedisOperations.<String, LastTransaction>opsForHash().get("wallettxn", id);
    }

    public Mono<LastTransaction> save(LastTransaction transaction) {
        if (transaction.getId() == null) {
            transaction.setId(transaction.getSourceWallet());
        }
        return this.reactiveRedisOperations.<String, LastTransaction>opsForHash()
                .put("wallettxn", transaction.getId(), transaction).log().map(p -> transaction);
    }

}
