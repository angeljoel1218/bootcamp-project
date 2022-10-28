package com.nttdata.bootcamp.creditsservice.controller;

import com.nttdata.bootcamp.creditsservice.application.CreditCardService;
import com.nttdata.bootcamp.creditsservice.model.CreditCard;
import com.nttdata.bootcamp.creditsservice.model.TransactionCreditCard;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import javax.validation.Valid;

@Slf4j
@RefreshScope
@RestController
@RequestMapping("/credit/card")
public class CreditCardController {

    @Autowired
    private CreditCardService creditCardService;

    @GetMapping()
    public Flux<CreditCard> findAll(){
        return creditCardService.findAll();
    }

    @GetMapping("/{id}")
    public Mono<CreditCard> findById(@PathVariable("id") String id){
        return creditCardService.findById(id);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.ACCEPTED)
    public  Mono<CreditCard> create(@RequestBody @Valid CreditCard creditMono){
        return  creditCardService.create(creditMono);
    }

    @PutMapping("/{id}")
    public  Mono<CreditCard> update(@RequestBody Mono<CreditCard> creditMono, @PathVariable String id){
        return  creditCardService.update(creditMono,id);
    }

    @DeleteMapping("/{id}")
    public  Mono<Void> delete(@PathVariable String id){
        return creditCardService.delete(id);
    }


    @PostMapping("/payment")
    public  Mono<ResponseEntity<String>> payment(@RequestBody TransactionCreditCard transactionCredit){
        return creditCardService.payment(transactionCredit).map(c -> ResponseEntity.ok().body(c)).onErrorResume(e -> {
            log.info("Error:" + e.getMessage());
            return Mono.just(ResponseEntity.badRequest().build());
        }).defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping("/charge")
    public  Mono<ResponseEntity<String>> charge(@RequestBody TransactionCreditCard transactionCredit){
        return creditCardService.charge(transactionCredit).map(c -> ResponseEntity.ok().body(c)).onErrorResume(e -> {
            log.info("Error:" + e.getMessage());
            return Mono.just(ResponseEntity.badRequest().build());
        }).defaultIfEmpty(ResponseEntity.notFound().build());
    }


    @GetMapping("/customer/{id}")
    public Flux<CreditCard>  findByIdCustomer(@PathVariable("id") String id){
        return  creditCardService.findByIdCustomer(id);
    }

    @GetMapping("/customer/transaction/{idCredit}")
    public Flux<TransactionCreditCard>  findTransactionByIdCredit(@PathVariable("idCredit") String idCredit){
        return  creditCardService.findTransactionByIdCredit(idCredit);
    }

    @GetMapping("/customer-debts/{idCustomer}")
    public Mono<Boolean>  findCustomerDebs(@PathVariable("idCustomer") String idCustomer){
        return  creditCardService.findIsCustomerHaveDebs(idCustomer);
    }
}
