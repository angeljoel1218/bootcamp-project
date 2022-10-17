package com.nttdata.bootcamp.creditsservice.application;

import com.nttdata.bootcamp.creditsservice.feingclients.CustumerFeingClient;
import com.nttdata.bootcamp.creditsservice.feingclients.ProductFeingClient;
import com.nttdata.bootcamp.creditsservice.infrastructure.CreditCardRepository;
import com.nttdata.bootcamp.creditsservice.infrastructure.TransactionCreditRepository;
import com.nttdata.bootcamp.creditsservice.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CreditCardServiceImpl implements CreditCardService {


    @Autowired
    private CustumerFeingClient custumerFeingClient;

    @Autowired
    private ProductFeingClient productFeingClient;

    @Autowired
    private TransactionCreditRepository transactionCreditRepository;


    @Autowired
    CreditCardRepository creditCardRepository;

    @Override
    public Mono<CreditCard> create(CreditCard creditCard) {
        List<String> erros= new ArrayList<>();

        Customer customer=  custumerFeingClient.findById(creditCard.getIdCustumer()).block();
        ProductCredit productCredit = productFeingClient.findById(creditCard.getIdProduct()).block();


        if(customer==null){
            erros.add("Cliente no existe");
        }
        if(productCredit == null){
            erros.add("Producto no existe");
        }

        if(customer.getIdType() == TypeCustomer.COMPANY && productCredit.getType()!= TypeCredit.BUSINESS_CREDIT){
            erros.add("El producto no esta disponible");
        }

        if(!erros.isEmpty()){
            return Mono.error(new InterruptedException(String.join(",",erros)  + CreditCard.class.getSimpleName()));
        }

        return Mono.just(creditCard) .flatMap(creditCardRepository::insert);
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

    @Override
    public Mono<TransactionCredit> payment(TransactionCredit transactionCredit) {
        List<String> erros = new ArrayList<>();

        CreditCard credit = creditCardRepository.findById(transactionCredit.getIdCredit()).block();

        if (credit == null) {
            erros.add("tarjeta de crédito no existe");
        }

        if(!erros.isEmpty()){
            return Mono.error(new InterruptedException(String.join(",",erros)  + CreditCard.class.getSimpleName()));
        }

        transactionCredit.setType(TypeTransaction.PAYMENT);
        return Mono.just(transactionCredit).flatMap(transactionCreditRepository::insert);
    }

    @Override
    public Mono<TransactionCredit> charge(TransactionCredit transactionCredit) {

        List<String> erros = new ArrayList<>();

        CreditCard credit = creditCardRepository.findById(transactionCredit.getIdCredit()).block();

        if (credit == null) {
            erros.add("tarjeta de crédito no existe");
        }

        BigDecimal totalCharge= transactionCreditRepository.findByIdCredit(credit.getId()).filter(t->t.getType()== TypeTransaction.CHARGE).map(t->t.getAmount())
                .reduce(BigDecimal::add).block();


        if(totalCharge.add(transactionCredit.getAmount()).compareTo(credit.getLimitAmount()) ==1){
            erros.add("Ha superado el limite permitido");
        };

        if(!erros.isEmpty()){
            return Mono.error(new InterruptedException(String.join(",",erros)  + CreditCard.class.getSimpleName()));
        }

        transactionCredit.setType(TypeTransaction.CHARGE);
        return   Mono.just(transactionCredit).flatMap(transactionCreditRepository::insert);
    }
}
