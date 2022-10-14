package com.nttdata.bootcamp.clientsservice.aplication;

import com.nttdata.bootcamp.clientsservice.infraestructure.ClientRepository;
import com.nttdata.bootcamp.clientsservice.model.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ClientServiceImpl implements  ClientService {

    @Autowired
    ClientRepository clientRepository;


    @Override
    public Mono<Client> saveClient(Mono<Client> clientMono) {
        return clientMono.flatMap(clientRepository::insert);
    }

    @Override
    public Flux<Client> findAll() {
        return clientRepository.findAll();
    }

    @Override
    public Mono<Client> getClient(String id) {
        return clientRepository.findById(id);
    }

    @Override
    public Mono<Client> updateClient(Mono<Client> clientMono, String id) {
        return clientRepository.findById(id)
                .flatMap(t->clientMono)
                .doOnNext(e->e.setId(id))
                .flatMap(clientRepository::save);
    }

    @Override
    public Mono<Void> deleteClient(String id) {
       return  clientRepository.deleteById(id);

    }

}
