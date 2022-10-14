package com.nttdata.bootcamp.clientsservice.controller;

import com.nttdata.bootcamp.clientsservice.aplication.ClientService;
import com.nttdata.bootcamp.clientsservice.aplication.PersonService;
import com.nttdata.bootcamp.clientsservice.model.Client;
import com.nttdata.bootcamp.clientsservice.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
public class PersonController {

    @Autowired
    private PersonService personService;


    @GetMapping("person")
    public Flux<Person> findAll(){
        return   personService.findAll();
    }

    @GetMapping("person/{id}")
    public Mono<Person> getClient(@PathVariable("id") String id){
        return personService.getPerson(id);
    }

    @PostMapping("person")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public  Mono<Person> savePerson(@RequestBody @Valid Mono<Person> personMono){
        return  personService.savePerson(personMono);
    }


    @PostMapping("person/update/{id}")
    public  Mono<Person> updatePerson(@RequestBody Mono<Person> personMono, @PathVariable String id){
        return  personService.updatePerson(personMono,id);
    }

    @PostMapping("person/delete/{id}")
    public  Mono<Void> personService(@PathVariable String id){
        return personService.deletePerson(id);
    }

}
