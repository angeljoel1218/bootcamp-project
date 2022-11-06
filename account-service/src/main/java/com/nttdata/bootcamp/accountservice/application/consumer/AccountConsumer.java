package com.nttdata.bootcamp.accountservice.application.consumer;

import com.nttdata.bootcamp.accountservice.application.producer.OrderBootcoinProducer;
import com.nttdata.bootcamp.accountservice.application.service.TransactionService;
import com.nttdata.bootcamp.accountservice.model.dto.TransactionBootcoinDto;
import com.nttdata.bootcamp.accountservice.model.dto.TransactionDto;
import com.nttdata.bootcamp.accountservice.model.dto.TransactionRequestDto;
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
public class AccountConsumer {

  @Autowired
  private TransactionService<TransactionDto> transactionService;


  @Autowired
  private OrderBootcoinProducer orderBootcoinProducer;

  @KafkaListener(topics = "${kafka.topic.bootcoin.account.transfer.name}")
  public void listener(@Payload TransactionBootcoinDto transactionBootcoinDto) {
    log.debug("Message received : {} ", transactionBootcoinDto);
    TransactionBootcoinDto result = applyBalance(transactionBootcoinDto).block();
    orderBootcoinProducer.sendMessage(result);
  }

  private Mono<TransactionBootcoinDto> applyBalance(TransactionBootcoinDto request) {
    log.debug("applyBalance executed : {} ", request);
    return transactionService.wireTransfer(TransactionRequestDto.builder()
        .amount(request.getAmount())
        .sourceAccount(request.getSourceNumber())
        .targetAccount(request.getTargetNumber())
      .build()).map(r -> {
      request.setStatus(TransactionBootcoinDto.Status.COMPLETED);
      return request;
    }).onErrorResume(throwable -> {
      request.setStatus(TransactionBootcoinDto.Status.PENDING);
      return Mono.just(request);
    });
	}
}
