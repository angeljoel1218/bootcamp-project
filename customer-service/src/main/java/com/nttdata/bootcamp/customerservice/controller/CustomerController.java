package com.nttdata.bootcamp.customerservice.controller;

import com.nttdata.bootcamp.customerservice.aplication.CustomerService;
import com.nttdata.bootcamp.customerservice.model.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@Slf4j
@RestController
public class CustomerController {


    @Value("${message.demo}")
    private String demoString;

    @Autowired
    private CustomerService clientService;


    @GetMapping("customer")
    public Flux<Customer> findAll(){
        log.info("Find All");
        log.info(demoString);
        return   clientService.findAll();
    }

    @GetMapping("customer/{id}")
    public Mono<ResponseEntity<Customer>> findById(@PathVariable("id") String id){
        return clientService.findById(id).map(client -> ResponseEntity.ok().body(client))
                .onErrorResume(e -> {
                    log.info(e.getMessage());
                    return Mono.just(ResponseEntity.badRequest().build());
                }).defaultIfEmpty(ResponseEntity.noContent().build());
    }

    @PostMapping("customer")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public  Mono<Customer> create(@RequestBody @Valid Mono<Customer> clientMono){
        return  clientService.create(clientMono);
    }


    @PostMapping("customer/update/{id}")
    public  Mono<Customer> update(@RequestBody Mono<Customer> clientMono, @PathVariable String id){
        return  clientService.update(clientMono,id);
    }

    @PostMapping("customer/delete/{id}")
    public  Mono<Void> delete(@PathVariable String id){
        return clientService.delete(id);
    }

}
