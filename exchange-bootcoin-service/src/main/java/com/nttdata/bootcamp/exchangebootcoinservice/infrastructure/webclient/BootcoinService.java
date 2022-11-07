package com.nttdata.bootcamp.exchangebootcoinservice.infrastructure.webclient;

import com.nttdata.bootcamp.exchangebootcoinservice.domain.dto.BootcoinDto;
import com.nttdata.bootcamp.exchangebootcoinservice.domain.dto.MethodPaymentDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class BootcoinService {
    @Autowired
    ReactiveCircuitBreakerFactory reactiveCircuitBreakerFactory;

    @Autowired
    WebClient webClientBuilder;

    public Mono<BootcoinDto> getWallet(String id) {
        return reactiveCircuitBreakerFactory.create("${circuitbreaker.instances.name}")
                .run(webClientBuilder
                        .get()
                        .uri("bootcoin/"+id)
                        .retrieve()
                        .bodyToMono(BootcoinDto.class), throwable -> Mono.error(new RuntimeException("The service is not available, try in a few minutes please")));
    }
}
