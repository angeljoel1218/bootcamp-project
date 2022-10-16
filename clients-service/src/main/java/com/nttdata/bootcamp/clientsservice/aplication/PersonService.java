package com.nttdata.bootcamp.clientsservice.aplication;

import com.nttdata.bootcamp.clientsservice.model.Client;
import com.nttdata.bootcamp.clientsservice.model.Person;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PersonService {

    Mono<Person> create(Mono<Person> personMono);

    Flux<Person> findAll();

    Mono<Person> findById(String id);

    Mono<Person> update(Mono<Person> personMono,String id);

    Mono<Void> delete(String id);

}
