package com.nttdata.bootcamp.creditsservice.application;

import com.nttdata.bootcamp.creditsservice.feingclients.CustumerFeingClient;
import com.nttdata.bootcamp.creditsservice.feingclients.ProductFeingClient;
import com.nttdata.bootcamp.creditsservice.infrastructure.CreditRepository;
import com.nttdata.bootcamp.creditsservice.infrastructure.PaymentCreditRepository;
import com.nttdata.bootcamp.creditsservice.infrastructure.TransactionCreditCardRepository;
import com.nttdata.bootcamp.creditsservice.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service

public class CreditServiceImpl implements CreditService {


    @Autowired
    private CreditRepository creditRepository;

    @Autowired
    private PaymentCreditRepository paymentCreditRepository;

    @Autowired
    private CustumerFeingClient custumerFeingClient;

    @Autowired
    private ProductFeingClient productFeingClient;


    @Override
    public Mono<Credit> create(Credit credit) {
        log.info("try to create");

        return custumerFeingClient.findById(credit.getIdCustomer()).flatMap(customer->{
            return
            productFeingClient.findById(credit.getIdProduct()).flatMap(product->{
                List<String> erros= new ArrayList<>();

                if(customer.getIdType() == TypeCustomer.COMPANY && product.getType()!= TypeCredit.BUSINESS_CREDIT){
                    erros.add("El producto no esta disponible");
                }

                if(customer.getIdType() == TypeCustomer.PERSONAL && product.getType()!= TypeCredit.PERSONAL_CREDIT){
                    erros.add("El producto no esta disponible");
                }
                return creditRepository.findByIdCustomer(customer.getId()).count().flatMap(cantidadCuentas->{
                    if(customer.getIdType() == TypeCustomer.PERSONAL ) {
                        if (cantidadCuentas > 0) {
                            erros.add("No puede tener mas de una cuenta");
                        }
                    }

                    if(!erros.isEmpty()){
                        return Mono.error(new InterruptedException(String.join(",",erros)  + Credit.class.getSimpleName()));
                    }

                    return  Mono.just(credit).flatMap(creditRepository::insert);
                });
            });
        });

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
    public Mono<PaymentCredit> payment(PaymentCredit payment) {

      return creditRepository.findById(payment.getIdCredit()).flatMap(credit -> {

            return Mono.just(payment).flatMap(paymentCreditRepository::insert);
        });
    }

    @Override
    public Mono<Customer> findCustomerById(String id) {
        return custumerFeingClient.findById(id);
    }


}
