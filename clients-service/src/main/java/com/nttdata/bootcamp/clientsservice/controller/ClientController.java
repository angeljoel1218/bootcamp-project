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
//@RequestMapping("/client")
public class ClientController {

    @Autowired
    private ClientService clientService;


    @GetMapping("client")
    public Flux<Client> getclients(){
        return   clientService.findAll();
    }

    @GetMapping("client/{id}")
    public Mono<Client> getClient(@PathVariable("id") String id){
        return clientService.getClient(id);
    }

    @PostMapping("client")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public  Mono<Client> saveClient(@RequestBody @Valid Mono<Client> clientMono){
        return  clientService.saveClient(clientMono);
    }


    @PostMapping("client/update/{id}")
    public  Mono<Client> updateClient(@RequestBody Mono<Client> clientMono, @PathVariable String id){
        return  clientService.updateClient(clientMono,id);
    }

    @PostMapping("client/delete/{id}")
    public  Mono<Void> deleteClient(@PathVariable String id){
        return clientService.deleteClient(id);
    }

}
