package com.nttdata.bootcamp.clientsservice.infraestructure;

import com.nttdata.bootcamp.clientsservice.model.Person;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface PersonRepository extends ReactiveMongoRepository<Person,String> {
}
