package com.nttdata.bootcamp.clientsservice.controller;

import com.nttdata.bootcamp.clientsservice.aplication.CompanyService;
import com.nttdata.bootcamp.clientsservice.aplication.PersonService;
import com.nttdata.bootcamp.clientsservice.model.Company;
import com.nttdata.bootcamp.clientsservice.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
public class CompanyController {

    @Autowired
    private CompanyService companyService;


    @GetMapping("company")
    public Flux<Company> findAll(){
        return   companyService.findAll();
    }

    @GetMapping("company/{id}")
    public Mono<Company> findById(@PathVariable("id") String id){
        return companyService.findById(id);
    }

    @PostMapping("company")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public  Mono<Company> saveCompany(@RequestBody @Valid Mono<Company> companyMono){
        return  companyService.create(companyMono);
    }

    @PostMapping("company/update/{id}")
    public  Mono<Company> updateCompany(@RequestBody Mono<Company> companyMono, @PathVariable String id){
        return  companyService.update(companyMono,id);
    }

    @PostMapping("company/delete/{id}")
    public  Mono<Void> deleteCompany(@PathVariable String id){
        return companyService.delete(id);
    }

}
