package com.nttdata.bootcamp.masterdatabootcoinservice.application.service.impl;

import com.nttdata.bootcamp.masterdatabootcoinservice.application.service.MethodPaymentService;
import com.nttdata.bootcamp.masterdatabootcoinservice.domain.MethodPayment;
import com.nttdata.bootcamp.masterdatabootcoinservice.infrastructure.MethodPaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class MethodPaymentServiceImpl implements MethodPaymentService {
    @Autowired
    MethodPaymentRepository methodPaymentRepository;

    @Override
    public Mono<MethodPayment> create(MethodPayment methodPayment) {
        return Mono.just(methodPayment)
                .flatMap(methodPaymentRepository::save);
    }

    @Override
    public Mono<MethodPayment> update(MethodPayment methodPayment, String id) {
        return methodPaymentRepository.findById(id)
                .flatMap(methodPayment1 -> {
                    methodPayment1.setValue(methodPayment.getValue());
                    return methodPaymentRepository.save(methodPayment1);
                });
    }

    @Override
    public Mono<MethodPayment> getMethodPayment(String id) {
        return methodPaymentRepository.findById(id)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Method payment not found")));
    }
}
