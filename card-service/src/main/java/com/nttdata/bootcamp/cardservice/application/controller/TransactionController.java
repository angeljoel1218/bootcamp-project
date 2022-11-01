package com.nttdata.bootcamp.cardservice.application.controller;

import com.nttdata.bootcamp.cardservice.application.TransactionService;
import com.nttdata.bootcamp.cardservice.model.CardMovement;
import com.nttdata.bootcamp.cardservice.model.dto.CardMovementDto;
import com.nttdata.bootcamp.cardservice.model.dto.PaymentDto;
import com.nttdata.bootcamp.cardservice.model.dto.WithdrawDto;
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
    @PostMapping("card/transaction/payment")
    public Mono<CardMovementDto> payment(@RequestBody PaymentDto paymentDto){
        return transactionService.payment(paymentDto);
    }

    @PostMapping("card/transaction/withdraw")
    public Mono<CardMovementDto> withdraw(@RequestBody WithdrawDto withdrawDto){
        return transactionService.withdraw(withdrawDto);
    }
}
