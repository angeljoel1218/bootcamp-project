package com.nttdata.bootcamp.adbootcoinservice.infrastructure.webclient;

import com.nttdata.bootcamp.adbootcoinservice.domain.dto.ExchangeRateDto;
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
public class MasterDataService {
    @Autowired
    ReactiveCircuitBreakerFactory reactiveCircuitBreakerFactory;

    @Autowired
    WebClient webClientBuilder;

    public Mono<ExchangeRateDto> getExchangeRate() {
        return reactiveCircuitBreakerFactory.create("${circuitbreaker.instances.name}")
                .run(webClientBuilder
                        .get()
                        .uri("data-bootcoin/exchange-rate")
                        .retrieve()
                        .bodyToMono(ExchangeRateDto.class),
                  throwable -> Mono.error(
                    new RuntimeException(
                      "The service is not available, try in a few minutes please")));
    }

}
