package com.nttdata.bootcamp.exchangebootcoinservice.infrastructure.events;

import com.nttdata.bootcamp.exchangebootcoinservice.application.service.TransactionService;
import com.nttdata.bootcamp.exchangebootcoinservice.domain.dto.TransactionBootcoinDto;
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
public class ConsumerTransaction {
    @Autowired
    TransactionService transactionService;

    @KafkaListener(topics = "${kafka.topic.transaction.result.name}", containerFactory = "transactionBootcoinDtoKafkaListenerContainerFactory")
    public void listener(@Payload TransactionBootcoinDto transactionDto) {
        log.debug("Message received : {} ", transactionDto);
        applyResult(transactionDto).block();
    }

    private Mono<Void> applyResult(TransactionBootcoinDto request) {
        log.debug("applyBalance executed : {} ", request);
        return transactionService.receiveTransactionConfirmation(request);
    }
}
