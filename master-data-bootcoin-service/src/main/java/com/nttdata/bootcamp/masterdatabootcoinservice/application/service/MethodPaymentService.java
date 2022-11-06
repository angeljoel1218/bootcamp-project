package com.nttdata.bootcamp.masterdatabootcoinservice.application.service;

import com.nttdata.bootcamp.masterdatabootcoinservice.domain.MethodPayment;
import reactor.core.publisher.Mono;

public interface MethodPaymentService {
    Mono<MethodPayment> create(MethodPayment methodPayment);
    Mono<MethodPayment> update(MethodPayment methodPayment, String id);
    Mono<MethodPayment> getMethodPayment(String id);
}
