package com.nttdata.bootcamp.walletservice.infrastructure.feignclient;

import com.nttdata.bootcamp.walletservice.model.dto.CardDto;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;

/**
 *
 * @since 2022
 */

@ReactiveFeignClient(name = "${feign.service.card.name}")
public interface CardClient {

  @RequestMapping(method = RequestMethod.GET, value = "card/get/{number}/{cvv}")
  Mono<CardDto> findCardByNumberAndCvv(@PathVariable("number") String number,
                                       @PathVariable("cvv") String cvv);


}
