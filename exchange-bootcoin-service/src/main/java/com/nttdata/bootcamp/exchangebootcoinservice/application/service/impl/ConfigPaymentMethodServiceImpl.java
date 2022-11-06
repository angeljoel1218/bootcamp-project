package com.nttdata.bootcamp.exchangebootcoinservice.application.service.impl;

import com.nttdata.bootcamp.exchangebootcoinservice.application.exception.ConfigMethodPaymentException;
import com.nttdata.bootcamp.exchangebootcoinservice.application.mapper.MapperConfigPaymentMethod;
import com.nttdata.bootcamp.exchangebootcoinservice.application.service.ConfigPaymentMethodService;
import com.nttdata.bootcamp.exchangebootcoinservice.domain.dto.ConfigPaymentMethodDto;
import com.nttdata.bootcamp.exchangebootcoinservice.domain.model.ConfigPaymentMethod;
import com.nttdata.bootcamp.exchangebootcoinservice.infrastructure.ConfigPaymentMethodRepository;
import com.nttdata.bootcamp.exchangebootcoinservice.infrastructure.webclient.MasterDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ConfigPaymentMethodServiceImpl implements ConfigPaymentMethodService {
    @Autowired
    ConfigPaymentMethodRepository configPaymentMethodRepository;
    @Autowired
    MapperConfigPaymentMethod mapperConfigPaymentMethod;
    @Autowired
    MasterDataService masterDataService;

    @Override
    public Mono<ConfigPaymentMethodDto> add(ConfigPaymentMethodDto configPaymentMethodDto) {
        return masterDataService.getMethodPayment(configPaymentMethodDto.getMethodPaymentId())
                .switchIfEmpty(Mono.error(new ConfigMethodPaymentException("Method payment not found")))
                .flatMap(methodPaymentDto -> Mono.just(configPaymentMethodDto)
                        .map(mapperConfigPaymentMethod::toConfigPaymentMethod)
                        .flatMap(configPaymentMethodRepository::insert)
                        .map(mapperConfigPaymentMethod::toDto));
    }

    @Override
    public Mono<ConfigPaymentMethodDto> update(ConfigPaymentMethodDto configPaymentMethodDto, String id) {
        ConfigPaymentMethod configPaymentMethod = mapperConfigPaymentMethod.toConfigPaymentMethod(configPaymentMethodDto);
        return masterDataService.getMethodPayment(configPaymentMethodDto.getId())
                .switchIfEmpty(Mono.error(new ConfigMethodPaymentException("Method payment not found")))
                .flatMap(method -> Mono.just(configPaymentMethod)
                        .doOnNext(config -> config.setId(id))
                        .flatMap(configPaymentMethodRepository::insert)
                        .map(mapperConfigPaymentMethod::toDto));
    }

    @Override
    public Mono<ConfigPaymentMethodDto> getMethodPayment(String id) {
        return configPaymentMethodRepository.findById(id)
                .map(mapperConfigPaymentMethod::toDto);
    }
}
