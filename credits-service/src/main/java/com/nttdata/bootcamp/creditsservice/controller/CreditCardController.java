package com.nttdata.bootcamp.creditsservice.controller;

import com.nttdata.bootcamp.creditsservice.application.CreditCardService;
import com.nttdata.bootcamp.creditsservice.model.CreditCard;
import com.nttdata.bootcamp.creditsservice.model.TransactionCreditCard;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@Slf4j
@RestController
public class CreditCardController {

    @Autowired
    private CreditCardService creditCardService;

    @GetMapping("creditcard")
    public Flux<CreditCard> findAll(){
        return   creditCardService.findAll();
    }

    @GetMapping("creditcard/{id}")
    public Mono<CreditCard> findById(@PathVariable("id") String id){
        return creditCardService.findById(id);
    }

    @PostMapping("creditcard")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public  Mono<CreditCard> create(@RequestBody @Valid CreditCard creditMono){
        return  creditCardService.create(creditMono);
    }

    @PostMapping("creditcard/update/{id}")
    public  Mono<CreditCard> update(@RequestBody Mono<CreditCard> creditMono, @PathVariable String id){
        return  creditCardService.update(creditMono,id);
    }

    @PostMapping("creditcard/delete/{id}")
    public  Mono<Void> delete(@PathVariable String id){
        return creditCardService.delete(id);
    }


    @PostMapping("creditcard/payment")
    public  Mono<ResponseEntity<TransactionCreditCard>> payment(@RequestBody TransactionCreditCard transactionCredit, @PathVariable String id){
        return creditCardService.payment(transactionCredit).map(c -> ResponseEntity.ok().body(c)).onErrorResume(e -> {
            log.info("Error:" + e.getMessage());
            return Mono.just(ResponseEntity.badRequest().build());

        }).defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping("creditcard/charge")
    public  Mono<ResponseEntity<TransactionCreditCard>> charge(@RequestBody TransactionCreditCard transactionCredit, @PathVariable String id){

        return creditCardService.charge(transactionCredit).map(c -> ResponseEntity.ok().body(c)).onErrorResume(e -> {
            log.info("Error:" + e.getMessage());
            return Mono.just(ResponseEntity.badRequest().build());

        }).defaultIfEmpty(ResponseEntity.notFound().build());
    }



}
