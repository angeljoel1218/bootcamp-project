package com.nttdata.bootcamp.cardservice.infrastructure.webclient;

import com.nttdata.bootcamp.cardservice.application.exception.ServiceUnavailableException;
import com.nttdata.bootcamp.cardservice.model.dto.CreditDuesDto;
import com.nttdata.bootcamp.cardservice.model.dto.CreditDuesRequestDto;
import com.nttdata.bootcamp.cardservice.model.dto.TransactionCreditDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 *
 * @since 2022
 */
@Component
public class CreditClient {
    @Autowired
    ReactiveCircuitBreakerFactory reactiveCircuitBreakerFactory;

    @Autowired
    WebClient webClientBuilder;

    public Mono<String> paymentCredit(CreditDuesRequestDto creditDuesRequestDto) {
        return reactiveCircuitBreakerFactory.create("${circuitbreaker.instances.name}")
                .run(webClientBuilder
                        .post()
                        .uri("credit/payment")
                        .body(Mono.just(creditDuesRequestDto), CreditDuesRequestDto.class)
                        .retrieve()
                        .bodyToMono(String.class),
                  throwable -> Mono.error(new ServiceUnavailableException(
                    "The service is not available, try in a few minutes please")));
    }

    public Mono<CreditDuesDto> paymentCreditCard(TransactionCreditDto transactionCreditDto) {
        return reactiveCircuitBreakerFactory.create("${circuitbreaker.instances.name}")
                .run(webClientBuilder
                        .post()
                        .uri("credit/card/payment")
                        .body(transactionCreditDto, TransactionCreditDto.class)
                        .retrieve()
                        .bodyToMono(CreditDuesDto.class),
                  throwable -> Mono.error(new ServiceUnavailableException(
                    "The service is not available, try in a few minutes please")));
    }
}
