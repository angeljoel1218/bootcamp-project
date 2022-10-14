package com.nttdata.bootcamp.clientsservice.aplication;

import com.nttdata.bootcamp.clientsservice.model.Client;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface ClientService {

    Mono<Client> saveClient(Mono<Client> clientMono);

    Flux<Client> findAll();

    Mono<Client> getClient(String id);

    Mono<Client> updateClient(Mono<Client> clientMono,String id);

    Mono<Void> deleteClient(String id);

}
