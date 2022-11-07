package com.nttdata.bootcamp.cardservice.application.controller;

import com.nttdata.bootcamp.cardservice.application.TransactionService;
import com.nttdata.bootcamp.cardservice.model.dto.CardMovementDto;
import com.nttdata.bootcamp.cardservice.model.dto.PaymentDto;
import com.nttdata.bootcamp.cardservice.model.dto.WithdrawDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 *
 * @since 2022
 */
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


  @GetMapping("card/transaction-lasted/{cardId}")
  public Mono<List<CardMovementDto>> findLastTenByCardIdOrderByOperationDateDesc(
    @PathVariable("cardId") String cardId) {
      return  transactionService.findLastByCardIdOrderByOperationDateDesc(cardId, 10);
    }
}
