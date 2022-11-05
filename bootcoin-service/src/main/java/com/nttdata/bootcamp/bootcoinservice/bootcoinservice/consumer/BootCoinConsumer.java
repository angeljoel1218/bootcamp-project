package com.nttdata.bootcamp.bootcoinservice.bootcoinservice.consumer;

import com.nttdata.bootcamp.bootcoinservice.bootcoinservice.application.BootcoinService;
import com.nttdata.bootcamp.bootcoinservice.bootcoinservice.model.dto.BootcoinDto;
import com.nttdata.bootcamp.bootcoinservice.bootcoinservice.model.dto.MovementsDto;
import com.nttdata.bootcamp.bootcoinservice.bootcoinservice.model.dto.TransactionDto;
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
public class BootCoinConsumer {

	@Autowired
	private BootcoinService bootcoinService;


	@KafkaListener(topics = "${kafka.topic.balance.name}")
	public void listener(@Payload TransactionDto transactionDto) {
		  log.debug("Message received : {} ", transactionDto);
		  applyBalance(transactionDto).block();
	}

	private Mono<BootcoinDto> applyBalance(TransactionDto request) {
		log.debug("applyBalance executed : {} ", request);
		return bootcoinService.addMovements(
         MovementsDto.builder()
        .phone(request.getSourceNumberCell())
        .description("seller")
        .amount(request.getAmount().negate()).build())
				.flatMap(walletDto -> bootcoinService.addMovements(
            MovementsDto.builder()
            .phone(request.getTargetNumberCell())
            .description("purchase")
            .amount(request.getAmount().negate()).build()));
	}
}
