package com.nttdata.bootcamp.creditsservice.controller;

import com.nttdata.bootcamp.creditsservice.application.CreditService;
import com.nttdata.bootcamp.creditsservice.model.Credit;
import com.nttdata.bootcamp.creditsservice.model.PaymentCredit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.HashMap;
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
        Map<String, Object> response = new HashMap<>();
        return creditService.create(creditMono).map(c -> ResponseEntity.ok().body(c)).onErrorResume(e -> {
            log.info("Error:" + e.getMessage());
            return Mono.just(ResponseEntity.badRequest().build());
        }).defaultIfEmpty(ResponseEntity.notFound().build());


    }

    @PostMapping("credits/payment")
    public  Mono<ResponseEntity<PaymentCredit>> payment(@RequestBody PaymentCredit paymentCredit){
        return creditService.payment(paymentCredit).map(c -> ResponseEntity.ok().body(c)).onErrorResume(e -> {
            log.info("Error:" + e.getMessage());
            return Mono.just(ResponseEntity.badRequest().build());
        }).defaultIfEmpty(ResponseEntity.notFound().build());

    }

    @GetMapping("credits/customer/{id}")
    public Flux<Credit>  findCustomerById(@PathVariable("id") String id){
            return  creditService.findByIdCustomer(id);
    }

    @GetMapping("credits/customer/payment/{idCredito}")
    public Flux<PaymentCredit>  findPaymentByIdCredit(@PathVariable("idCredito") String id){
        return  creditService.findPaymentByIdCredit(id);

    }




}
