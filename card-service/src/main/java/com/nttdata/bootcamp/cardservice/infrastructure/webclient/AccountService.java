package com.nttdata.bootcamp.cardservice.infrastructure.webclient;

import com.nttdata.bootcamp.cardservice.application.exception.CardException;
import com.nttdata.bootcamp.cardservice.application.exception.InsufficientBalanceException;
import com.nttdata.bootcamp.cardservice.application.exception.ServiceUnavailableException;
import com.nttdata.bootcamp.cardservice.model.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *
 * @since 2022
 */
@Component
public class AccountService {
    @Autowired
    ReactiveCircuitBreakerFactory reactiveCircuitBreakerFactory;

    @Autowired
    WebClient webClientBuilder;

    public Flux<AccountDto> findByHolderId(String holderId) {
        return reactiveCircuitBreakerFactory.create("${circuitbreaker.instances.name}")
          .run(webClientBuilder
            .get()
            .uri("account/customer/" + holderId)
            .retrieve()
            .bodyToFlux(AccountDto.class),
            throwable -> Flux.error(new ServiceUnavailableException(
              "The service is not available, try in a few minutes please")));
    }

    public Mono<AccountDto> findByNumber(String number) {
        return reactiveCircuitBreakerFactory.create("${circuitbreaker.instances.name}")
                .run(webClientBuilder
                    .get()
                    .uri("account/" + number)
                    .retrieve()
                    .bodyToMono(AccountDto.class),
                  throwable -> Mono.error(new ServiceUnavailableException(
                    "The service is not available, try in a few minutes please")));
    }

    public Mono<TransactionDto> withdraw(WithdrawTxnDto withdrawDto) {
        return reactiveCircuitBreakerFactory.create("${circuitbreaker.instances.name}")
                .run(webClientBuilder
                  .put()
                  .uri("/account/transaction/withdraw")
                  .body(Mono.just(withdrawDto), WithdrawTxnDto.class)
                  .retrieve()
                  .onStatus(HttpStatus::isError, clientResponse -> clientResponse
                    .bodyToMono(ApiError.class)
                    .flatMap(body -> {
                      if (body.getCode().equals(ResponseCode.INSUFFICIENT_BALANCE)){
                        return Mono.error(new InsufficientBalanceException(body.getMessage()));
                      }
                      return Mono.error(new CardException(body.getMessage()));
                    }))
                  .bodyToMono(TransactionDto.class));
    }
}
