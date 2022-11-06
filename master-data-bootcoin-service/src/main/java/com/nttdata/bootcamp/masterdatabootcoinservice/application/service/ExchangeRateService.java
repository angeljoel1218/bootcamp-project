package com.nttdata.bootcamp.masterdatabootcoinservice.application.service;

import com.nttdata.bootcamp.masterdatabootcoinservice.domain.ExchangeRate;
import reactor.core.publisher.Mono;

public interface ExchangeRateService {
    Mono<ExchangeRate> create(ExchangeRate exchangeRate);
    Mono<ExchangeRate> update(ExchangeRate exchangeRate, String id);
    Mono<ExchangeRate> getExchangeRate(String id);
    Mono<ExchangeRate> getLastExchangeRate();
}
