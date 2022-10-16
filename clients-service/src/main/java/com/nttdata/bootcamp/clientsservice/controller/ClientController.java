package com.nttdata.bootcamp.clientsservice.controller;

import com.nttdata.bootcamp.clientsservice.aplication.ClientService;
import com.nttdata.bootcamp.clientsservice.model.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
public class ClientController {

    @Autowired
    private ClientService clientService;


    @GetMapping("client")
    public Flux<Client> findAll(){
        return   clientService.findAll();
    }

    @GetMapping("client/{id}")
    public Mono<Client> findById(@PathVariable("id") String id){
        return clientService.findById(id);
    }

    @PostMapping("client")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public  Mono<Client> create(@RequestBody @Valid Mono<Client> clientMono){
        return  clientService.create(clientMono);
    }


    @PostMapping("client/update/{id}")
    public  Mono<Client> update(@RequestBody Mono<Client> clientMono, @PathVariable String id){
        return  clientService.update(clientMono,id);
    }

    @PostMapping("client/delete/{id}")
    public  Mono<Void> delete(@PathVariable String id){
        return clientService.delete(id);
    }

}
