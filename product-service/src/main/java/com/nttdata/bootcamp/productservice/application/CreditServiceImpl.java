package com.nttdata.bootcamp.productservice.application;

import com.nttdata.bootcamp.productservice.infrastructure.CreditRepository;
import com.nttdata.bootcamp.productservice.model.Credit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CreditServiceImpl implements CreditService{

    @Autowired
    CreditRepository creditRepository;

    @Override
    public Mono<Credit> create(Mono<Credit> credit) {
        return credit.flatMap(creditRepository::insert);
    }

    @Override
    public Mono<Credit> update(String id, Credit credit) {
        return this.findById(id).flatMap(c -> {
            c.setName(credit.getName());
            c.setMaxNumber(credit.getMaxNumber());
            c.setType(credit.getType());
            c.setCoin(credit.getCoin());
            return creditRepository.save(c);
        });
    }

    @Override
    public Mono<Void> delete(String id) {
        return this.findById(id).flatMap(creditRepository::delete);
    }

    @Override
    public Mono<Credit> findById(String id) {
        return creditRepository.findById(id);
    }

    @Override
    public Flux<Credit> findAll() {
        return creditRepository.findAll();
    }

}
