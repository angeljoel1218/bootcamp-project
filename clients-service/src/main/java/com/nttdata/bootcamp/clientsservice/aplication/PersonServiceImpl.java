package com.nttdata.bootcamp.clientsservice.aplication;

import com.nttdata.bootcamp.clientsservice.infraestructure.PersonRepository;
import com.nttdata.bootcamp.clientsservice.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PersonServiceImpl  implements PersonService {


    @Autowired
    PersonRepository personRepository;

    @Override
    public Mono<Person> savePerson(Mono<Person> personMono) {
        return personMono.flatMap(personRepository::insert);
    }

    @Override
    public Flux<Person> findAll() {
        return personRepository.findAll();
    }

    @Override
    public Mono<Person> getPerson(String id) {
        return personRepository.findById(id);
    }

    @Override
    public Mono<Person> updatePerson(Mono<Person> personMono, String id) {
         return personRepository.findById(id)
                .flatMap(t->personMono)
                .doOnNext(e->e.setId(id))
                .flatMap(personRepository::save);
    }

    @Override
    public Mono<Void> deletePerson(String id) {
        return personRepository.deleteById(id);
    }
}
