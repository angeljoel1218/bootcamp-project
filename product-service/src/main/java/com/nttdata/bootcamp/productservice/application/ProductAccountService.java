package com.nttdata.bootcamp.productservice.application;

import com.nttdata.bootcamp.productservice.model.dto.ProductAccountDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductAccountService {
    Mono<ProductAccountDto> create(ProductAccountDto account);
    Mono<ProductAccountDto> update(String id, ProductAccountDto ProductAccountDto);
    Mono<Void> delete(String id);
    Mono<ProductAccountDto> findById(String id);
    Flux<ProductAccountDto> findAll();
}
