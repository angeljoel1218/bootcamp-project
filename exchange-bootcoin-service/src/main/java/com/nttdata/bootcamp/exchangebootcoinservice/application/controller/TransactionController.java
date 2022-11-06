package com.nttdata.bootcamp.exchangebootcoinservice.application.controller;

import com.nttdata.bootcamp.exchangebootcoinservice.application.service.TransactionService;
import com.nttdata.bootcamp.exchangebootcoinservice.domain.dto.TransactionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RefreshScope
@RestController
public class TransactionController {
    @Autowired
    TransactionService transactionService;

    @PutMapping("exchange/transaction/accept/{orderId}")
    public Mono<TransactionDto> accept(@PathVariable("orderId") String orderId){
        return transactionService.acceptRequest(orderId);
    }
}
