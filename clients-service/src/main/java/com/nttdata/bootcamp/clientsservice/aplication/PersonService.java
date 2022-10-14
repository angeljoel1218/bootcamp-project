package com.nttdata.bootcamp.clientsservice.aplication;

import com.nttdata.bootcamp.clientsservice.model.Client;
import com.nttdata.bootcamp.clientsservice.model.Person;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PersonService {

    Mono<Person> savePerson(Mono<Person> personMono);

    Flux<Person> findAll();

    Mono<Person> getPerson(String id);

    Mono<Person> updatePerson(Mono<Person> personMono,String id);

    Mono<Void> deletePerson(String id);

}
