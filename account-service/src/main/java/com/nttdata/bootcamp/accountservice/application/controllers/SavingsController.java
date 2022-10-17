package com.nttdata.bootcamp.accountservice.application.controllers;

import com.nttdata.bootcamp.accountservice.application.AccountService;
import com.nttdata.bootcamp.accountservice.model.dto.OperationDto;
import com.nttdata.bootcamp.accountservice.model.dto.SavingsAccountDto;
import com.nttdata.bootcamp.accountservice.model.dto.TransactionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
public class SavingsController {
    @Autowired
    AccountService<SavingsAccountDto> accountService;

    @PostMapping("savings")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<SavingsAccountDto> create(@Valid @RequestBody SavingsAccountDto savingsAccountDto){
        return accountService.create(savingsAccountDto);
    }

    @DeleteMapping("savings/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable String id) {
        return accountService.delete(id)
                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)))
                .defaultIfEmpty(new ResponseEntity<Void>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("savings/{number}")
    public Mono<ResponseEntity<SavingsAccountDto>> findByNumber(@PathVariable String number){
        return accountService.findByNumber(number)
                .map(a -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(a))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("savings/{accountId}/transactions")
    public Flux<TransactionDto> listTransactions(@PathVariable String accountId){
        return accountService.listTransactions(accountId);
    }

    @PutMapping("savings/deposit")
    public Mono<String> deposit(@PathVariable OperationDto depositDto){
        return accountService.deposit(depositDto);
    }

    @PutMapping("savings/withdraw")
    public Mono<String> withdraw(@PathVariable OperationDto depositDto){
        return accountService.withdraw(depositDto);
    }
}
