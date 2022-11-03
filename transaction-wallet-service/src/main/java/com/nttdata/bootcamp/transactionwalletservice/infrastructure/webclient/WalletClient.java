package com.nttdata.bootcamp.transactionwalletservice.infrastructure.webclient;

import com.nttdata.bootcamp.transactionwalletservice.application.exception.ServiceUnavailableException;
import com.nttdata.bootcamp.transactionwalletservice.model.dto.WalletDto;
import com.nttdata.bootcamp.transactionwalletservice.model.dto.WalletTransactionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

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
                                .uri(""+cellNumber)
                                .retrieve()
                                .bodyToMono(WalletDto.class),
                        throwable -> Mono.error(new ServiceUnavailableException("The service is not available, try in a few minutes please")));
    }

    public Mono<WalletTransactionDto> withdrawals(WalletTransactionDto walletTransactionDto) {
        return reactiveCircuitBreakerFactory.create("${circuitbreaker.instances.name}")
                .run(
                        webClientBuilder
                                .post()
                                .uri("")
                                .body(walletTransactionDto, WalletTransactionDto.class)
                                .retrieve()
                                .bodyToMono(WalletTransactionDto.class),
                        throwable -> Mono.error(new ServiceUnavailableException("The service is not available, try in a few minutes please")));
    }

    public Mono<WalletTransactionDto> depositMoney(WalletTransactionDto walletTransactionDto) {
        return reactiveCircuitBreakerFactory.create("${circuitbreaker.instances.name}")
                .run(
                        webClientBuilder
                                .post()
                                .uri("")
                                .body(walletTransactionDto, WalletTransactionDto.class)
                                .retrieve()
                                .bodyToMono(WalletTransactionDto.class),
                        throwable -> Mono.error(new ServiceUnavailableException("The service is not available, try in a few minutes please")));
    }
}
