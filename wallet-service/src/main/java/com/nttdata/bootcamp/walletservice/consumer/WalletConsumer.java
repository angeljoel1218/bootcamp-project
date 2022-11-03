package com.nttdata.bootcamp.walletservice.consumer;

import com.nttdata.bootcamp.walletservice.application.WalletService;
import com.nttdata.bootcamp.walletservice.model.Wallet;
import com.nttdata.bootcamp.walletservice.model.dto.TransactionDto;
import com.nttdata.bootcamp.walletservice.model.dto.WalletDto;
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
public class WalletConsumer {

	@Autowired
	private WalletService walletService;


	@KafkaListener(topics = "${kafka.topic.balance.name}")
	public void listener(@Payload TransactionDto transactionDto) {
		  log.debug("Message received : {} ", transactionDto);
		  applyBalance(transactionDto).block();
	}

	private Mono<WalletDto> applyBalance(TransactionDto request) {
		log.debug("applyBalance executed : {} ", request);
		return walletService.findByPhone(request.getPhone()).flatMap(wallet -> {
			 return walletService.setBalance(wallet.getPhone(),request.getAmount());
		});
	}
}
