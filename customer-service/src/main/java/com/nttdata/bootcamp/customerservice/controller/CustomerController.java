package com.nttdata.bootcamp.customerservice.controller;

import com.nttdata.bootcamp.customerservice.aplication.CustomerService;
import com.nttdata.bootcamp.customerservice.model.Customer;
import com.nttdata.bootcamp.customerservice.model.dto.CustomerDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@Slf4j
@RefreshScope
@RestController
public class CustomerController {


    @Value("${message.demo}")
    private String demoString;

    @Autowired
    private CustomerService custumerService;


    @PostMapping("customer")
    @ResponseStatus(HttpStatus.CREATED)
    public  Mono<CustomerDto> create(@RequestBody @Valid CustomerDto customerDto){
        return  custumerService.create(customerDto);
    }

    @PostMapping("customer/{id}")
    public  Mono<CustomerDto> update(@RequestBody @Valid CustomerDto customerDto, @PathVariable String id){
        return  custumerService.update(customerDto,id);
    }

    @PostMapping("customer/delete/{id}")
    public  Mono<Void> delete(@PathVariable String id){
        return custumerService.delete(id);
    }


    @GetMapping("customer/{id}")
    public Mono<ResponseEntity<CustomerDto>> findById(@PathVariable("id") String id){
        return custumerService.findById(id)
                .map(a -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(a))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("customer")
    public Flux<CustomerDto> findAll(){
        log.info("Find All");
        log.info(demoString);
        return   custumerService.findAll();
    }





}
