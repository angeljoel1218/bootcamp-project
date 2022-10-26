package com.nttdata.bootcamp.accountservice.application.controllers;

import com.nttdata.bootcamp.accountservice.application.SingleAccountService;
import com.nttdata.bootcamp.accountservice.application.OperationService;
import com.nttdata.bootcamp.accountservice.model.dto.OperationDto;
import com.nttdata.bootcamp.accountservice.model.dto.SavingsAccountDto;
import com.nttdata.bootcamp.accountservice.model.dto.TransactionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RefreshScope
@RestController
public class SavingsController {
    @Autowired
    SingleAccountService<SavingsAccountDto> accountService;

    @Autowired
    OperationService<SavingsAccountDto> operationService;

    @PostMapping("account/savings")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<SavingsAccountDto> create(@Valid @RequestBody SavingsAccountDto savingsAccountDto){
        return accountService.create(savingsAccountDto);
    }

    @DeleteMapping("account/savings/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable String id) {
        return accountService.delete(id)
                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)))
                .defaultIfEmpty(new ResponseEntity<Void>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("account/savings/{number}")
    public Mono<ResponseEntity<SavingsAccountDto>> findByNumber(@PathVariable String number){
        return accountService.findByNumber(number)
                .map(a -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(a))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("account/savings/{accountId}/transactions")
    public Flux<TransactionDto> listTransactions(@PathVariable String accountId){
        return accountService.listTransactions(accountId);
    }

    @PutMapping("account/savings/deposit")
    public Mono<String> deposit(@RequestBody OperationDto depositDto){
        return operationService.deposit(depositDto);
    }

    @PutMapping("account/savings/withdraw")
    public Mono<String> withdraw(@RequestBody OperationDto depositDto){
        return operationService.withdraw(depositDto);
    }

    @PostMapping("account/savings/transfer")
    public Mono<String> wireTransfer(@RequestBody OperationDto depositDto){
        return operationService.wireTransfer(depositDto);
    }
}
