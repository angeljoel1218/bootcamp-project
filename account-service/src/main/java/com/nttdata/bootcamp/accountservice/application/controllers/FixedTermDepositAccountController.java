package com.nttdata.bootcamp.accountservice.application.controllers;

import com.nttdata.bootcamp.accountservice.application.AccountService;
import com.nttdata.bootcamp.accountservice.model.dto.CurrentAccountDto;
import com.nttdata.bootcamp.accountservice.model.dto.FixedTermDepositAccountDto;
import com.nttdata.bootcamp.accountservice.model.dto.OperationDto;
import com.nttdata.bootcamp.accountservice.model.dto.TransactionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
public class FixedTermDepositAccountController {
    @Autowired
    AccountService<FixedTermDepositAccountDto> accountService;

    @PostMapping("fixed-term-deposit")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<FixedTermDepositAccountDto> create(@Valid @RequestBody FixedTermDepositAccountDto fixedTermDepositAccountDto){
        return accountService.create(fixedTermDepositAccountDto);
    }

    @DeleteMapping("fixed-term-deposit/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable String id) {
        return accountService.delete(id)
                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)))
                .defaultIfEmpty(new ResponseEntity<Void>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("fixed-term-deposit/{number}")
    public Mono<ResponseEntity<FixedTermDepositAccountDto>> findByNumber(@PathVariable String number){
        return accountService.findByNumber(number)
                .map(a -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(a))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("fixed-term-deposit/{accountId}/transactions")
    public Flux<TransactionDto> listTransactions(@PathVariable String accountId){
        return accountService.listTransactions(accountId);
    }

    @PutMapping("fixed-term-deposit/deposit")
    public Mono<String> deposit(@PathVariable OperationDto depositDto){
        return accountService.deposit(depositDto);
    }

    @PutMapping("fixed-term-deposit/withdraw")
    public Mono<String> withdraw(@PathVariable OperationDto depositDto){
        return accountService.withdraw(depositDto);
    }
}
