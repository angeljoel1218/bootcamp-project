package com.nttdata.bootcamp.masterdatabootcoinservice.application.controller;

import com.nttdata.bootcamp.masterdatabootcoinservice.application.service.MethodPaymentService;
import com.nttdata.bootcamp.masterdatabootcoinservice.domain.MethodPayment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 *
 * @since 2022
 */

@RefreshScope
@RestController
public class MethodPaymentController {
    @Autowired
    MethodPaymentService methodPaymentService;

    @PostMapping("data-bootcoin/method-payment/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<MethodPayment> create(@RequestBody MethodPayment methodPayment){
        return methodPaymentService.create(methodPayment);
    }

    @PutMapping("data-bootcoin/method-payment/{id}")
    public Mono<MethodPayment> update(@PathVariable("id") String id,
                                      @RequestBody MethodPayment methodPayment){
        return methodPaymentService.update(methodPayment, id);
    }

    @GetMapping("data-bootcoin/method-payment/{id}")
    public Mono<MethodPayment> getMethodPayment(@PathVariable("id") String id){
        return methodPaymentService.getMethodPayment(id);
    }
}
