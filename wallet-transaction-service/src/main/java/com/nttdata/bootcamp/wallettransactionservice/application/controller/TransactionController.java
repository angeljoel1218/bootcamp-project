package com.nttdata.bootcamp.wallettransactionservice.application.controller;

import com.nttdata.bootcamp.wallettransactionservice.application.servivce.TransactionService;
import com.nttdata.bootcamp.wallettransactionservice.model.dto.TransactionDto;
import com.nttdata.bootcamp.wallettransactionservice.model.dto.TransactionRequestDto;
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
    public Mono<TransactionDto> transfer(@RequestBody TransactionRequestDto transactionRequestDto){
        return transactionService.transfer(transactionRequestDto);
    }
}
