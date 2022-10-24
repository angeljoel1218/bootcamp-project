package com.nttdata.bootcamp.productservice.application;

import com.nttdata.bootcamp.productservice.model.dto.ProductTypeDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductTypeService {
    Mono<ProductTypeDto> create(ProductTypeDto productTypeDto);
    Mono<ProductTypeDto> update(String id, ProductTypeDto productTypeDto);
    Mono<Void> delete(String id);
    Mono<ProductTypeDto> findById(String id);
    Flux<ProductTypeDto> findAll();
}
