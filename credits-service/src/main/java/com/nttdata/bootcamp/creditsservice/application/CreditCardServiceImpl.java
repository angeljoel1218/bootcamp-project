package com.nttdata.bootcamp.creditsservice.application;

import com.nttdata.bootcamp.creditsservice.infrastructure.CreditCardRepository;
import com.nttdata.bootcamp.creditsservice.model.CreditCard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CreditCardServiceImpl implements CreditCardService {


    @Autowired
    CreditCardRepository creditCardRepository;

    @Override
    public Mono<CreditCard> create(Mono<CreditCard> creditCardMono) {
        return creditCardMono.flatMap(creditCardRepository::insert);
    }

    @Override
    public Mono<CreditCard> update(Mono<CreditCard> creditCardMono, String id) {
        return creditCardRepository.findById(id)
                .flatMap(t -> creditCardMono)
                .doOnNext(e -> e.setId(id))
                .flatMap(creditCardRepository::save);
    }

    @Override
    public Mono<Void> delete(String id) {
        return creditCardRepository.deleteById(id);
    }

    @Override
    public Mono<CreditCard> findById(String id) {
        return creditCardRepository.findById(id);
    }

    @Override
    public Flux<CreditCard> findAll() {
        return creditCardRepository.findAll();
    }
}
