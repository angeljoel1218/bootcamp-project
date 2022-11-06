package com.nttdata.bootcamp.masterdatabootcoinservice.infrastructure;

import com.nttdata.bootcamp.masterdatabootcoinservice.domain.ExchangeRate;
import com.nttdata.bootcamp.masterdatabootcoinservice.domain.MethodPayment;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class MethodPaymentRepository {
    private final ReactiveRedisOperations<String, MethodPayment> reactiveRedisOperations;

    public Mono<MethodPayment> findById(String id) {
        return this.reactiveRedisOperations.<String, MethodPayment>opsForHash().get("payments", id);
    }

    public Mono<MethodPayment> save(MethodPayment methodPayment) {
        if (methodPayment.getId() == null) {
            String id = UUID.randomUUID().toString();
            methodPayment.setId(id);
        }
        return this.reactiveRedisOperations.<String, MethodPayment>opsForHash()
                .put("payments", methodPayment.getId(), methodPayment).log().map(p -> methodPayment);
    }
}
