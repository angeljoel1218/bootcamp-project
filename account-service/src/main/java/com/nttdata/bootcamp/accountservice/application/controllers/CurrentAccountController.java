package com.nttdata.bootcamp.accountservice.application.controllers;

import com.nttdata.bootcamp.accountservice.application.ManyAccountService;
import com.nttdata.bootcamp.accountservice.application.OperationService;
import com.nttdata.bootcamp.accountservice.model.dto.CurrentAccountDto;
import com.nttdata.bootcamp.accountservice.model.dto.OperationDto;
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
public class CurrentAccountController {
    @Autowired
    ManyAccountService<CurrentAccountDto> accountService;

    @Autowired
    OperationService<CurrentAccountDto> operationService;

    @PostMapping("account/current-account")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<CurrentAccountDto> create(@Valid @RequestBody CurrentAccountDto currentAccountDto){
        return accountService.create(currentAccountDto);
    }

    @DeleteMapping("account/current-account/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable String id) {
        return accountService.delete(id)
                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)))
                .defaultIfEmpty(new ResponseEntity<Void>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("account/current-account/{number}")
    public Mono<ResponseEntity<CurrentAccountDto>> findByNumber(@PathVariable String number){
        return accountService.findByNumber(number)
                .map(a -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(a))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("account/current-account/{accountId}/transactions")
    public Flux<TransactionDto> listTransactions(@PathVariable String accountId){
        return accountService.listTransactions(accountId);
    }

    @GetMapping("account/current-account/customer/{holderId}")
    public Flux<CurrentAccountDto> findByHolderId(@PathVariable("holderId") String holderId){
        return accountService.findByHolderId(holderId);
    }

    @PutMapping("account/current-account/deposit")
    public Mono<String> deposit(@RequestBody OperationDto operationDto){
        return operationService.deposit(operationDto);
    }

    @PutMapping("account/current-account/withdraw")
    public Mono<String> withdraw(@RequestBody OperationDto operationDto){
        return operationService.withdraw(operationDto);
    }

    @PostMapping("account/current-account/transfer")
    public Mono<String> transfer(@RequestBody OperationDto operationDto) {
        return operationService.wireTransfer(operationDto);
    }
}
