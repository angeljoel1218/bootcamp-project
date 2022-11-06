package com.nttdata.bootcamp.exchangebootcoinservice.application.controller;

import com.nttdata.bootcamp.exchangebootcoinservice.application.service.ConfigPaymentMethodService;
import com.nttdata.bootcamp.exchangebootcoinservice.domain.dto.ConfigPaymentMethodDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RefreshScope
@RestController
public class ConfigPaymentMethodController {
    @Autowired
    ConfigPaymentMethodService configPaymentMethodService;

    @PostMapping("exchange/config-payment")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ConfigPaymentMethodDto> add(@RequestBody ConfigPaymentMethodDto configPaymentMethodDto){
        return configPaymentMethodService.add(configPaymentMethodDto);
    }

    @PutMapping("exchange/config-payment/{id}")
    public Mono<ConfigPaymentMethodDto> update(@PathVariable("id") String id, @RequestBody ConfigPaymentMethodDto configPaymentMethodDto) {
        return configPaymentMethodService.update(configPaymentMethodDto, id);
    }

    @GetMapping("exchange/config-payment/{id}")
    public Mono<ConfigPaymentMethodDto> requestPurchase(@PathVariable("id") String id){
        return configPaymentMethodService.getMethodPayment(id);
    }
}
