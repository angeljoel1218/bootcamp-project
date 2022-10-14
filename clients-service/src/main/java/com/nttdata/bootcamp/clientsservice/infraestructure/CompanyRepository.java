package com.nttdata.bootcamp.clientsservice.infraestructure;

import com.nttdata.bootcamp.clientsservice.model.Company;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CompanyRepository extends ReactiveMongoRepository<Company,String> {

}
