package com.nttdata.bootcamp.wallettransactionservice.infrastructure.consumer;

import com.nttdata.bootcamp.wallettransactionservice.application.servivce.TransactionService;
import com.nttdata.bootcamp.wallettransactionservice.infrastructure.producer.OrderBootcoinProducer;
import com.nttdata.bootcamp.wallettransactionservice.model.dto.TransactionBootcoinDto;
import com.nttdata.bootcamp.wallettransactionservice.model.dto.TransactionRequestDto;
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
public class WalletTransactionConsumer {

  @Autowired
	private TransactionService transactionService;

  @Autowired
  private   OrderBootcoinProducer orderBootcoinProducer;


  @KafkaListener(topics = "${kafka.topic.wallet.transaction.name}")
  public void listener(@Payload TransactionBootcoinDto transactionDto) {
    log.debug("Message received : {} ", transactionDto);
    TransactionBootcoinDto result = applyBalance(transactionDto).block();
    orderBootcoinProducer.sendMessage(result);
  }

  private Mono<TransactionBootcoinDto> applyBalance(TransactionBootcoinDto request) {
    log.debug("applyBalance executed : {} ", request);
    return transactionService.transfer(TransactionRequestDto.builder()
      .sourceNumberCell(request.getSourceNumber())
      .targetNumberCell(request.getTargetNumber())
      .amount(request.getAmount()).build()).map(r -> {
        request.setStatus(TransactionBootcoinDto.Status.COMPLETED);
        return request;
      }).onErrorResume(throwable -> {
        request.setStatus(TransactionBootcoinDto.Status.PENDING);
        return Mono.just(request);
      });
  }
}
