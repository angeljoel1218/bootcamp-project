package com.nttdata.bootcamp.walletservice.infrastructure.feignclient;

import com.nttdata.bootcamp.walletservice.model.dto.CardDto;
import javax.naming.ServiceUnavailableException;
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
public class CardService {
  @Autowired
  CardClient cardClient;

  @Autowired
  ReactiveCircuitBreakerFactory reactiveCircuitBreakerFactory;

  @Autowired
  private WebClient webClientBuilder;


  public Mono<CardDto> findCardByNumberAndCvv(String number, String cvv) {
    return reactiveCircuitBreakerFactory.create("${circuitbreaker.instances.name}")
                .run(cardClient.findCardByNumberAndCvv(number, cvv),
                  throwable ->
                  Mono.error(new ServiceUnavailableException(
                    "The service is not available, try in a few minutes please")));
  }


}
