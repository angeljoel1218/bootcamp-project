package com.nttdata.bootcamp.exchangebootcoinservice.infrastructure.events;

import com.nttdata.bootcamp.exchangebootcoinservice.application.service.TransactionService;
import com.nttdata.bootcamp.exchangebootcoinservice.domain.dto.PayOrderDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class ConsumerPurchaseRequest {
    @Autowired
    TransactionService transactionService;

    @KafkaListener(topics = "${kafka.topic.bootcoin.purchase.request}", containerFactory = "kafkaListenerContainerFactory")
    public void listener(@Payload PayOrderDto payOrderDto) {
        log.debug("Message received : {} ", payOrderDto);
        applyRequest(payOrderDto).block();
    }

    private Mono<PayOrderDto> applyRequest(PayOrderDto payOrderDto) {
        log.debug("applyRequest executed : {} ", payOrderDto);
        return transactionService.purchaseRequest(payOrderDto);
    }
}
