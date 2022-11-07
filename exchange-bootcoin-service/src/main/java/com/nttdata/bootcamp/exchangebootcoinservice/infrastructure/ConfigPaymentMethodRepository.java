package com.nttdata.bootcamp.exchangebootcoinservice.infrastructure;

import com.nttdata.bootcamp.exchangebootcoinservice.domain.model.ConfigPaymentMethod;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface ConfigPaymentMethodRepository extends ReactiveMongoRepository<ConfigPaymentMethod, String> {
    Mono<ConfigPaymentMethod> findByMethodPaymentId(String id);
    Mono<ConfigPaymentMethod> findByWalletId(String walletId);
}
