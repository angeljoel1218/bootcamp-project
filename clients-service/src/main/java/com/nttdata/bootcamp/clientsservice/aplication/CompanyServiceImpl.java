package com.nttdata.bootcamp.clientsservice.aplication;

import com.nttdata.bootcamp.clientsservice.infraestructure.ClientRepository;
import com.nttdata.bootcamp.clientsservice.infraestructure.CompanyRepository;
import com.nttdata.bootcamp.clientsservice.model.Client;
import com.nttdata.bootcamp.clientsservice.model.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CompanyServiceImpl implements   CompanyService{


    @Autowired
    CompanyRepository companyRepository;


    @Override
    public Mono<Company> saveCompany(Mono<Company> companyMono) {
        return companyMono.flatMap(companyRepository::insert);
    }

    @Override
    public Flux<Company> findAll() {
        return companyRepository.findAll();
    }

    @Override
    public Mono<Company> getCompany(String id) {
        return companyRepository.findById(id);
    }

    @Override
    public Mono<Company> updateCompany(Mono<Company> companyMono, String id) {
        return companyRepository.findById(id)
                .flatMap(t->companyMono)
                .doOnNext(e->e.setId(id))
                .flatMap(companyRepository::save);
    }

    @Override
    public Mono<Void> deleteCompany(String id) {
        return companyRepository.deleteById(id);
    }
}
