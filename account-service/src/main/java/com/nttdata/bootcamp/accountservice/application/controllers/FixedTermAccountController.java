package com.nttdata.bootcamp.accountservice.application.controllers;

import com.nttdata.bootcamp.accountservice.application.SingleAccountService;
import com.nttdata.bootcamp.accountservice.application.OperationService;
import com.nttdata.bootcamp.accountservice.model.dto.FixedTermAccountDto;
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
public class FixedTermAccountController {
    @Autowired
    SingleAccountService<FixedTermAccountDto> accountService;
    @Autowired
    OperationService<FixedTermAccountDto> operationService;

    @PostMapping("account/fixed-term-deposit")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<FixedTermAccountDto> create(@Valid @RequestBody FixedTermAccountDto fixedTermAccountDto){
        return accountService.create(fixedTermAccountDto);
    }

    @DeleteMapping("account/fixed-term-deposit/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable String id) {
        return accountService.delete(id)
                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)))
                .defaultIfEmpty(new ResponseEntity<Void>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("account/fixed-term-deposit/{number}")
    public Mono<ResponseEntity<FixedTermAccountDto>> findByNumber(@PathVariable String number){
        return accountService.findByNumber(number)
                .map(a -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(a))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("account/fixed-term-deposit/{accountId}/transactions")
    public Flux<TransactionDto> listTransactions(@PathVariable String accountId){
        return accountService.listTransactions(accountId);
    }

    @GetMapping("account/fixed-term-deposit/customer/{holderId}")
    public Mono<ResponseEntity<FixedTermAccountDto>> findByHolderId(@PathVariable("holderId") String holderId){
        return accountService.findByHolderId(holderId)
                .map(a -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(a))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("account/fixed-term-deposit/deposit")
    public Mono<String> deposit(@RequestBody OperationDto operationDto){
        return operationService.deposit(operationDto);
    }

    @PutMapping("account/fixed-term-deposit/withdraw")
    public Mono<String> withdraw(@RequestBody OperationDto operationDto){
        return operationService.withdraw(operationDto);
    }

    @PostMapping("account/fixed-term-deposit/transfer")
    public Mono<String> transfer(@RequestBody OperationDto operationDto) {
        return operationService.wireTransfer(operationDto);
    }
}
