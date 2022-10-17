package com.nttdata.bootcamp.clientsservice.aplication;

import com.nttdata.bootcamp.clientsservice.model.Client;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface ClientService {

    Mono<Client> create(Mono<Client> clientMono);

    Flux<Client> findAll();

    Mono<Client> findById(String id);

    Mono<Client> update(Mono<Client> clientMono,String id);

    Mono<Void> delete(String id);

}
