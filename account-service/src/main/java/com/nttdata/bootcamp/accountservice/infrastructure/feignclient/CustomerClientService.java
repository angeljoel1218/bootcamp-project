package com.nttdata.bootcamp.accountservice.infrastructure.feignclient;

import com.nttdata.bootcamp.accountservice.application.exceptions.ServiceUnavailableException;
import com.nttdata.bootcamp.accountservice.model.dto.CustomerDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CustomerClientService {
    @Autowired
    CustomerClient customerClient;

    ReactiveCircuitBreakerFactory reactiveCircuitBreakerFactory;

    public Mono<CustomerDto> getCustomer(String id) {
        return reactiveCircuitBreakerFactory.create("${circuitbreaker.instances.name}")
                .run(customerClient.getCustomer(id), throwable -> Mono.error(new ServiceUnavailableException("The service is not available, try in a few minutes please")));
    }
}
