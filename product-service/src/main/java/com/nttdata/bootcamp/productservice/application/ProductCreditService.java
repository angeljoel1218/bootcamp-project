package com.nttdata.bootcamp.productservice.application;

import com.nttdata.bootcamp.productservice.model.dto.ProductCreditDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductCreditService {
    Mono<ProductCreditDto> create(ProductCreditDto credit);
    Mono<ProductCreditDto> update(String id, ProductCreditDto ProductCreditDto);
    Mono<Void> delete(String id);
    Mono<ProductCreditDto> findById(String id);
    Flux<ProductCreditDto> findAll();
}
