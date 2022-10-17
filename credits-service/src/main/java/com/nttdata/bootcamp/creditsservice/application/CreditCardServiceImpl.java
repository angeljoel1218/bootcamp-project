package com.nttdata.bootcamp.creditsservice.application;

import com.nttdata.bootcamp.creditsservice.feingclients.CustumerFeingClient;
import com.nttdata.bootcamp.creditsservice.feingclients.ProductFeingClient;
import com.nttdata.bootcamp.creditsservice.infrastructure.CreditCardRepository;
import com.nttdata.bootcamp.creditsservice.infrastructure.TransactionCreditCardRepository;
import com.nttdata.bootcamp.creditsservice.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class CreditCardServiceImpl implements CreditCardService {


    @Autowired
    private CustumerFeingClient custumerFeingClient;

    @Autowired
    private ProductFeingClient productFeingClient;

    @Autowired
    private TransactionCreditCardRepository transactionCreditRepository;


    @Autowired
    CreditCardRepository creditCardRepository;

    @Override
    public Mono<CreditCard> create(CreditCard creditCard) {
        log.info("try to create");
        return custumerFeingClient.findById(creditCard.getIdCustomer()).flatMap(customer->{
            return
                    productFeingClient.findById(creditCard.getIdProduct()).flatMap(product->{
                        List<String> erros= new ArrayList<>();

                        if(customer.getIdType() == TypeCustomer.COMPANY && product.getType()!= TypeCredit.BUSINESS_CREDIT_CARD){
                            erros.add("El producto no esta disponible");
                        }

                        if(customer.getIdType() == TypeCustomer.PERSONAL && product.getType()!= TypeCredit.PERSONAL_CREDIT_CARD){
                            erros.add("El producto no esta disponible");
                        }

                        if(!erros.isEmpty()){
                            return Mono.error(new InterruptedException(String.join(",",erros)  + Credit.class.getSimpleName()));
                        }

                         return  Mono.just(creditCard).flatMap(creditCardRepository::insert);
                    });
        });
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
    public Mono<TransactionCreditCard> payment(TransactionCreditCard transactionCredit) {
        return creditCardRepository.findById(transactionCredit.getIdCredit()).flatMap(credit -> {
            transactionCredit.setType(TypeTransaction.CHARGE);
            return Mono.just(transactionCredit).flatMap(transactionCreditRepository::insert);
        });


    }

    @Override
    public Mono<TransactionCreditCard> charge(TransactionCreditCard transactionCredit) {

        return creditCardRepository.findById(transactionCredit.getIdCredit()).flatMap(credit -> {

            return
             transactionCreditRepository.findByIdCredit(credit.getId()).filter(t->t.getType()== TypeTransaction.CHARGE).map(t->t.getAmount())
                    .reduce(BigDecimal::add).flatMap(sumCharge->{

                         List<String> erros= new ArrayList<>();

                         if(sumCharge.add(transactionCredit.getAmount()).compareTo(credit.getLimitAmount()) ==1){
                             erros.add("Ha superado el limite permitido");
                         };

                         if(!erros.isEmpty()){
                             return Mono.error(new InterruptedException(String.join(",",erros)  + CreditCard.class.getSimpleName()));
                         }

                         transactionCredit.setType(TypeTransaction.CHARGE);
                         return Mono.just(transactionCredit).flatMap(transactionCreditRepository::insert);
                     });

        });




    }

    @Override
    public Flux<CreditCard> findByIdCustomer(String id) {
        return creditCardRepository.findByIdCustomer(id);
    }

    @Override
    public Flux<TransactionCreditCard> findTransactionByIdCredit(String id) {
        return transactionCreditRepository.findByIdCredit(id);
    }
}
