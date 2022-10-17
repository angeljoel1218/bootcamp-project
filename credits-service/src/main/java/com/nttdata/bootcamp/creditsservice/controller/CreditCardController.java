package com.nttdata.bootcamp.creditsservice.controller;

import com.nttdata.bootcamp.creditsservice.application.CreditCardService;
import com.nttdata.bootcamp.creditsservice.model.CreditCard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

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
    public  Mono<CreditCard> create(@RequestBody @Valid Mono<CreditCard> creditMono){
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



}
