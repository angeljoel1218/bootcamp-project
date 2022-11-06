package com.nttdata.bootcamp.exchangebootcoinservice.infrastructure.webclient;

import com.nttdata.bootcamp.exchangebootcoinservice.domain.dto.MethodPaymentDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class MasterDataService {
    @Autowired
    ReactiveCircuitBreakerFactory reactiveCircuitBreakerFactory;

    @Autowired
    WebClient webClientBuilder;

    public Mono<MethodPaymentDto> getMethodPayment(String id) {
        return reactiveCircuitBreakerFactory.create("${circuitbreaker.instances.name}")
                .run(webClientBuilder
                        .get()
                        .uri("data-bootcoin/method-payment/"+id)
                        .retrieve()
                        .bodyToMono(MethodPaymentDto.class), throwable -> Mono.error(new RuntimeException("The service is not available, try in a few minutes please")));
    }

}
