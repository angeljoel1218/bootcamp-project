package com.nttdata.bootcamp.clientsservice.infraestructure;

import com.nttdata.bootcamp.clientsservice.model.Client;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends ReactiveMongoRepository<Client, String> {

}
