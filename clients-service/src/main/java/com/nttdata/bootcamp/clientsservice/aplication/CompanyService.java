package com.nttdata.bootcamp.clientsservice.aplication;

import com.nttdata.bootcamp.clientsservice.model.Client;
import com.nttdata.bootcamp.clientsservice.model.Company;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CompanyService {

    Mono<Company> create(Mono<Company> companyMono);

    Flux<Company> findAll();

    Mono<Company> findById(String id);

    Mono<Company> update(Mono<Company> companyMono,String id);

    Mono<Void> delete(String id);

}
