package com.nttdata.bootcamp.transactionwalletservice.application.controller;

import com.nttdata.bootcamp.transactionwalletservice.application.servivce.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RefreshScope
@RestController
public class TransactionController {
    @Autowired
    TransactionService transactionService;

    @PostMapping("wallet-transfer")
    public Mono<CardMovementDto> transfer(@RequestBody PaymentDto paymentDto){
        return transactionService.transfer();
    }
}
