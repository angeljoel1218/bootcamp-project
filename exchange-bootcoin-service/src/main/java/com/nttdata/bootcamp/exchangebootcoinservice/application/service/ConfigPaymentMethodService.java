package com.nttdata.bootcamp.exchangebootcoinservice.application.service;

import com.nttdata.bootcamp.exchangebootcoinservice.domain.dto.ConfigPaymentMethodDto;
import reactor.core.publisher.Mono;

public interface ConfigPaymentMethodService {
    Mono<ConfigPaymentMethodDto> add(ConfigPaymentMethodDto configPaymentMethodDto);
    Mono<ConfigPaymentMethodDto> update(ConfigPaymentMethodDto configPaymentMethodDto, String id);
    Mono<ConfigPaymentMethodDto> getMethodPayment(String id);
}
