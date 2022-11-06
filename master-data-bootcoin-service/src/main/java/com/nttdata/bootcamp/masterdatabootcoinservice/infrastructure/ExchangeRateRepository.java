package com.nttdata.bootcamp.masterdatabootcoinservice.infrastructure;

import com.nttdata.bootcamp.masterdatabootcoinservice.domain.ExchangeRate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ExchangeRateRepository {
    private final ReactiveRedisOperations<String, ExchangeRate> reactiveRedisOperations;

    public Mono<ExchangeRate> findById(String id) {
        return this.reactiveRedisOperations.<String, ExchangeRate>opsForHash().get("exchanges", id);
    }

    public Mono<ExchangeRate> save(ExchangeRate exchangeRate) {
        if (exchangeRate.getId() == null) {
            String id = UUID.randomUUID().toString();
            exchangeRate.setId(id);
        }
        return this.reactiveRedisOperations.<String, ExchangeRate>opsForHash()
                .put("exchanges", exchangeRate.getId(), exchangeRate).log().map(p -> exchangeRate);
    }

    public Flux<ExchangeRate> findAll() {
        return this.reactiveRedisOperations.<String, ExchangeRate>opsForHash().values("exchanges");
    }

}
