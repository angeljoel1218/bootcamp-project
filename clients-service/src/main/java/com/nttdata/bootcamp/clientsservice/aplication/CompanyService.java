package com.nttdata.bootcamp.clientsservice.aplication;

import com.nttdata.bootcamp.clientsservice.model.Client;
import com.nttdata.bootcamp.clientsservice.model.Company;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CompanyService {

    Mono<Company> saveCompany(Mono<Company> companyMono);

    Flux<Company> findAll();

    Mono<Company> getCompany(String id);

    Mono<Company> updateCompany(Mono<Company> companyMono,String id);

    Mono<Void> deleteCompany(String id);

}
