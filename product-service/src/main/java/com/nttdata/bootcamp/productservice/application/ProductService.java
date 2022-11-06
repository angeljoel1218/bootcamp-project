package com.nttdata.bootcamp.productservice.application;

import com.nttdata.bootcamp.productservice.model.dto.ProductDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Some javadoc.
 * @since 2022
 */
public interface ProductService {
    Mono<ProductDto> create(ProductDto productDto);
    Mono<ProductDto> update(String id, ProductDto productDto);
    Mono<Void> delete(String id);
    Mono<ProductDto> findById(String id);
    Flux<ProductDto> findAll();
}
