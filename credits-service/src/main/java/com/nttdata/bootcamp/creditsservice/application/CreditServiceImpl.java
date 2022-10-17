package com.nttdata.bootcamp.creditsservice.application;

import com.nttdata.bootcamp.creditsservice.feingclients.CustumerFeingClient;
import com.nttdata.bootcamp.creditsservice.feingclients.ProductFeingClient;
import com.nttdata.bootcamp.creditsservice.infrastructure.CreditRepository;
import com.nttdata.bootcamp.creditsservice.infrastructure.TransactionCreditRepository;
import com.nttdata.bootcamp.creditsservice.model.*;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.apache.bcel.generic.ReturnaddressType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service

public class CreditServiceImpl implements CreditService {


    @Autowired
    private CreditRepository creditRepository;

    @Autowired
    private TransactionCreditRepository transactionCreditRepository;

    @Autowired
    private CustumerFeingClient custumerFeingClient;

    @Autowired
    private ProductFeingClient productFeingClient;


    @Override
    public Mono<Credit> create(Credit creditMono) {
        List<String> erros= new ArrayList<>();


        Customer customer=    custumerFeingClient.findById(creditMono.getIdCustomer()).block();
        ProductCredit productCredit = productFeingClient.findById(creditMono.getIdProduct()).block();

        if(customer==null){
            erros.add("Cliente no existe");
        }
        if(productCredit == null){
            erros.add("Producto no existe");
        }

        if(customer.getIdType() == TypeCustomer.COMPANY && productCredit.getType()!= TypeCredit.BUSINESS_CREDIT){
            erros.add("El producto no esta disponible");
        }

        if(customer.getIdType() == TypeCustomer.PERSONAL && productCredit.getType()!= TypeCredit.PERSONAL_CREDIT){
            erros.add("El producto no esta disponible");
        }

        if(customer.getIdType() == TypeCustomer.PERSONAL ){
           Mono<Long> cuenta= creditRepository.findByIdCustomer(customer.getId()).count();
           if(cuenta.block()>0){
               erros.add("No puede tener mas de una cuenta");
           };
        }

        if(!erros.isEmpty()){
            return Mono.error(new InterruptedException(String.join(",",erros)  + Credit.class.getSimpleName()));
        }

        return Mono.just(creditMono).flatMap(creditRepository::insert);

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
    public Mono<TransactionCredit> payment(TransactionCredit TransactionCredit) {
        List<String> erros = new ArrayList<>();

        Credit credit = creditRepository.findById(TransactionCredit.getIdCredit()).block();
        if (credit == null) {
            erros.add("crédito no existe");
        }


        if(!erros.isEmpty()){
            return Mono.error(new InterruptedException(String.join(",",erros)  + Credit.class.getSimpleName()));
        }


        return Mono.just(TransactionCredit).flatMap(transactionCreditRepository::insert);
    }


}
