package com.nttdata.bootcamp.creditsservice.controller;

import com.nttdata.bootcamp.creditsservice.application.CreditService;
import com.nttdata.bootcamp.creditsservice.model.Client;
import com.nttdata.bootcamp.creditsservice.model.Credit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

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
    public  Mono<Credit> create(@RequestBody @Valid Mono<Credit> creditMono){
        return  creditService.create(creditMono);
    }


    @PostMapping("credits/update/{id}")
    public  Mono<Credit> update(@RequestBody Mono<Credit> creditMono, @PathVariable String id){
        return  creditService.update(creditMono,id);
    }

    @PostMapping("credits/delete/{id}")
    public  Mono<Void> delete(@PathVariable String id){
        return creditService.delete(id);
    }

    @GetMapping("credits/clients/{id}")
    public  Mono<Object> findClientById(@PathVariable String id){
        return creditService.findClientById(id);
    }

}
