package com.nttdata.bootcamp.wallettransactionservice.infrastructure.webclient;

import com.nttdata.bootcamp.wallettransactionservice.application.exception.ServiceUnavailableException;
import com.nttdata.bootcamp.wallettransactionservice.model.dto.WalletDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Component
public class WalletClient {
    @Autowired
    ReactiveCircuitBreakerFactory reactiveCircuitBreakerFactory;

    @Autowired
    WebClient webClientBuilder;

    public Mono<WalletDto> getWallet(String cellNumber) {
        return reactiveCircuitBreakerFactory.create("${circuitbreaker.instances.name}")
                .run(
                        webClientBuilder
                                .get()
                                .uri("wallet/get/"+cellNumber)
                                .retrieve()
                                .bodyToMono(WalletDto.class),
                        throwable -> Mono.error(new ServiceUnavailableException("The service is not available, try in a few minutes please")));
    }

    public Mono<WalletDto> setBalance(String phone, BigDecimal amount) {
        return reactiveCircuitBreakerFactory.create("${circuitbreaker.instances.name}")
                .run(
                        webClientBuilder
                                .put()
                                .uri("wallet/set-balance/"+phone+"/" + amount)
                                .retrieve()
                                .bodyToMono(WalletDto.class),
                        throwable -> Mono.error(new ServiceUnavailableException("The service is not available, try in a few minutes please")));
    }
}
