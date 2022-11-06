package com.nttdata.bootcamp.masterdatabootcoinservice.application.controller;

import com.nttdata.bootcamp.masterdatabootcoinservice.application.service.ExchangeRateService;
import com.nttdata.bootcamp.masterdatabootcoinservice.domain.ExchangeRate;
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
public class ExchangeRateController {
    @Autowired
    ExchangeRateService exchangeRateService;

    @PostMapping("data-bootcoin/exchange-rate/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ExchangeRate> create(@RequestBody ExchangeRate exchangeRate){
        return exchangeRateService.create(exchangeRate);
    }

    @PutMapping("data-bootcoin/exchange-rate/{id}")
    public Mono<ExchangeRate> update(@PathVariable("id") String id,
                                     @RequestBody ExchangeRate exchangeRate){
        return exchangeRateService.update(exchangeRate, id);
    }

    @GetMapping("data-bootcoin/exchange-rate/{id}")
    public Mono<ExchangeRate> getExchangeRate(@PathVariable("id") String id){
        return exchangeRateService.getExchangeRate(id);
    }

    @GetMapping("data-bootcoin/exchange-rate")
    public Mono<ExchangeRate> getLastExchangeRate(){
        return exchangeRateService.getLastExchangeRate();
    }
}
