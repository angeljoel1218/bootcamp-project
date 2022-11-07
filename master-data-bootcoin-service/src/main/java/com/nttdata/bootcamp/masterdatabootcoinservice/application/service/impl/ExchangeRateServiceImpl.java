package com.nttdata.bootcamp.masterdatabootcoinservice.application.service.impl;

import com.nttdata.bootcamp.masterdatabootcoinservice.application.service.ExchangeRateService;
import com.nttdata.bootcamp.masterdatabootcoinservice.domain.ExchangeRate;
import com.nttdata.bootcamp.masterdatabootcoinservice.infrastructure.ExchangeRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 *
 * @since 2022
 */

@Service
public class ExchangeRateServiceImpl implements ExchangeRateService {
    @Autowired
    ExchangeRateRepository exchangeRateRepository;

    @Override
    public Mono<ExchangeRate> create(ExchangeRate exchangeRate) {
        return Mono.just(exchangeRate)
                .flatMap(exchangeRateRepository::save);
    }

    @Override
    public Mono<ExchangeRate> update(ExchangeRate exchangeRate, String id) {
        return exchangeRateRepository.findById(id)
                .flatMap(exchangeRate1 -> {
                   exchangeRate1.setCurrency(exchangeRate.getCurrency());
                   exchangeRate1.setPrice(exchangeRate.getPrice());
                   exchangeRate1.setRefCurrency(exchangeRate.getRefCurrency());
                   return exchangeRateRepository.save(exchangeRate1);
                });
    }

    @Override
    public Mono<ExchangeRate> getExchangeRate(String id) {
        return exchangeRateRepository.findById(id)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Exchange rate not found")));
    }

    @Override
    public Mono<ExchangeRate> getLastExchangeRate() {
        return exchangeRateRepository
                .findAll()
                .last();
    }
}
