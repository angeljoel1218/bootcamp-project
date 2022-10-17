package com.nttdata.bootcamp.creditsservice.controller;

import com.nttdata.bootcamp.creditsservice.application.CreditService;
import com.nttdata.bootcamp.creditsservice.model.Credit;
import com.nttdata.bootcamp.creditsservice.model.CreditCard;
import com.nttdata.bootcamp.creditsservice.model.Customer;
import com.nttdata.bootcamp.creditsservice.model.TransactionCredit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.Map;

@Slf4j
@RestController
public class CreditController {

    @Autowired
    private CreditService creditService;


    @GetMapping("credits")
    public Flux<Credit> findAll(){
        return   creditService.findAll();
    }

    @GetMapping("credits/{id}")
    public Mono<Credit> findById(@PathVariable("id") String id){
        return creditService.findById(id);
    }

    @PostMapping("credits")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public  Mono<ResponseEntity<Credit>> create(@RequestBody @Valid Credit creditMono){
        return creditService.create(creditMono).map(c -> ResponseEntity.ok().body(c)).onErrorResume(e -> {
            log.info("Error:" + e.getMessage());
            return Mono.just(ResponseEntity.badRequest().build());
        });

    }

    @PostMapping("credits/payment")
    public  Mono<ResponseEntity<TransactionCredit>> payment(@RequestBody TransactionCredit transactionCredit, @PathVariable String id){

        return creditService.payment(transactionCredit).map(c -> ResponseEntity.ok().body(c)).onErrorResume(e -> {
            log.info("Error:" + e.getMessage());
            return Mono.just(ResponseEntity.badRequest().build());

        });
    }

}
