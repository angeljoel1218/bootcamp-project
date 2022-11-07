package com.nttdata.bootcamp.adbootcoinservice.infrastructure.events;

import com.nttdata.bootcamp.adbootcoinservice.application.service.AdvertService;
import com.nttdata.bootcamp.adbootcoinservice.domain.dto.AdvertDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;


/**
 * Some javadoc.
 *
 * @since 2022
 */


@Component
@Slf4j
@RequiredArgsConstructor
public class AdvertStatusConsumer {

  @Autowired
  private AdvertService advertService;

  @KafkaListener(topics = "${kafka.topic.accept.purchase}")
  public void listener(@Payload String id) {
    log.debug("Message received : {} ", id);
    applyBalance(id).block();
  }

  private Mono<AdvertDto> applyBalance(String id) {
    log.debug("applyBalance executed : {} ", id);

    return advertService.acceptPurchase(id);
  }
}
