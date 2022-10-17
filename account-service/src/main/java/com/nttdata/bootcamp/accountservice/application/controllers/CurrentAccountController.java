package com.nttdata.bootcamp.accountservice.application.controllers;

import com.nttdata.bootcamp.accountservice.application.AccountService;
import com.nttdata.bootcamp.accountservice.model.dto.CurrentAccountDto;
import com.nttdata.bootcamp.accountservice.model.dto.OperationDto;
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
public class CurrentAccountController {
    @Autowired
    AccountService<CurrentAccountDto> accountService;

    @PostMapping("current-account")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<CurrentAccountDto> create(@Valid @RequestBody CurrentAccountDto currentAccountDto){
        return accountService.create(currentAccountDto);
    }

    @DeleteMapping("current-account/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable String id) {
        return accountService.delete(id)
                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)))
                .defaultIfEmpty(new ResponseEntity<Void>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("current-account/{number}")
    public Mono<ResponseEntity<CurrentAccountDto>> findByNumber(@PathVariable String number){
        return accountService.findByNumber(number)
                .map(a -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(a))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("current-account/{accountId}/transactions")
    public Flux<TransactionDto> listTransactions(@PathVariable String accountId){
        return accountService.listTransactions(accountId);
    }

    @PutMapping("current-account/deposit")
    public Mono<String> deposit(@PathVariable OperationDto depositDto){
        return accountService.deposit(depositDto);
    }

    @PutMapping("current-account/withdraw")
    public Mono<String> withdraw(@PathVariable OperationDto depositDto){
        return accountService.withdraw(depositDto);
    }
}
