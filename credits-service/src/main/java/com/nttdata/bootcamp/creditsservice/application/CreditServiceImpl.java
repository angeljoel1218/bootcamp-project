package com.nttdata.bootcamp.creditsservice.application;

import com.nttdata.bootcamp.creditsservice.feingclients.ClientFeingClient;
import com.nttdata.bootcamp.creditsservice.infrastructure.CreditRepository;
import com.nttdata.bootcamp.creditsservice.model.Client;
import com.nttdata.bootcamp.creditsservice.model.Credit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CreditServiceImpl implements CreditService {


    @Autowired
    private CreditRepository creditRepository;

    @Autowired
    private ClientFeingClient clientFeingClient;

    @Override
    public Mono<Credit> create(Mono<Credit> creditMono) {
        return creditMono.flatMap(creditRepository::insert);
    }

    @Override
    public Mono<Credit> update(Mono<Credit> creditMono, String id) {
        return creditRepository.findById(id)
                .flatMap(t -> creditMono)
                .doOnNext(e -> e.setId(id))
                .flatMap(creditRepository::save);
    }

    @Override
    public Mono<Void> delete(String id) {
        return creditRepository .deleteById(id);
    }

    @Override
    public Mono<Credit> findById(String id) {
        return creditRepository.findById(id);
    }

    @Override
    public Flux<Credit> findAll() {
        return creditRepository.findAll();
    }

    @Override
    public Mono<Object> findClientById(String id) {
        return    clientFeingClient.findById(id)  ;
    }
}
