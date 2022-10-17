package com.nttdata.bootcamp.customerservice.aplication;

import com.nttdata.bootcamp.customerservice.infraestructure.CustomerRepository;
import com.nttdata.bootcamp.customerservice.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    CustomerRepository costumerRepository;


    @Override
    public Mono<Customer> create(Mono<Customer> costumerMono) {
        return costumerMono.flatMap(costumerRepository::insert);
    }

    @Override
    public Flux<Customer> findAll() {
        return costumerRepository.findAll();
    }

    @Override
    public Mono<Customer> findById(String id) {
        return costumerRepository.findById(id);
    }

    @Override
    public Mono<Customer> update(Mono<Customer> costumerMono, String id) {
        return costumerRepository.findById(id)
                .flatMap(t->costumerMono)
                .doOnNext(e->e.setId(id))
                .flatMap(costumerRepository::save);
    }

    @Override
    public Mono<Void> delete(String id) {
       return  costumerRepository.deleteById(id);

    }

}
