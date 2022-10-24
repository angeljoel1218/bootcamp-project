package com.nttdata.bootcamp.creditsservice.application;

import com.nttdata.bootcamp.creditsservice.feignclients.CustomerFeignClient;
import com.nttdata.bootcamp.creditsservice.feignclients.ProductFeignClient;
import com.nttdata.bootcamp.creditsservice.infrastructure.CreditRepository;
import com.nttdata.bootcamp.creditsservice.infrastructure.PaymentCreditRepository;
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
    private CustomerFeignClient custumerFeingClient;

    @Autowired
    private ProductFeignClient productFeingClient;


    @Override
    public Mono<Credit> create(Credit credit) {
        log.info("try to create");

        return custumerFeingClient.findById(credit.getIdCustomer()).flatMap(customer->{
            return
            productFeingClient.findById(credit.getIdProduct()).flatMap(product->{
                List<String> erros= new ArrayList<>();

                if(customer.isItsCompany() && product.getCategory() != Category.ACTIVE){
                    erros.add("El producto no esta disponible");
                }

                if(customer.isItsPersonal() && product.getCategory() !=  Category.ACTIVE){
                    erros.add("El producto no esta disponible");
                }
                return creditRepository.findByIdCustomer(credit.getIdCustomer()).count().flatMap(cantidadCuentas->{
                    if(customer.isItsPersonal() ) {
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
    public Flux<Credit> findByIdCustomer(String id) {
        return creditRepository.findByIdCustomer(id);
    }

    @Override
    public Flux<PaymentCredit> findPaymentByIdCredit(String id) {
        return paymentCreditRepository.findByIdCredit(id);
    }


}
