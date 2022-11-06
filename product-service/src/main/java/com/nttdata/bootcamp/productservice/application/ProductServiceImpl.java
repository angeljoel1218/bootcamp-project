package com.nttdata.bootcamp.productservice.application;

import com.nttdata.bootcamp.productservice.application.mappers.MapperProduct;
import com.nttdata.bootcamp.productservice.infrastructure.ProductRepository;
import com.nttdata.bootcamp.productservice.model.dto.ProductDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Some javadoc.
 * @since 2022
 */
@Slf4j
@Service
public class ProductServiceImpl implements ProductService{
    @Autowired
    MapperProduct mapperProduct;

    @Autowired
    ProductRepository productRepository;

    @Override
    public Mono<ProductDto> create(ProductDto productDto) {
        return Mono.just(productDto)
                .map(mapperProduct::toProduct)
                .flatMap(productRepository::insert)
                .map(mapperProduct::toDto);
    }

    @Override
    public Mono<ProductDto> update(String id, ProductDto productDto) {
        return productRepository.findById(id)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Product not found")))
                .flatMap(product -> Mono.just(productDto).map(mapperProduct::toProduct))
                .doOnNext(product -> product.setId(id))
                .flatMap(productRepository::save)
                .map(mapperProduct::toDto);
    }

    @Override
    public Mono<Void> delete(String id) {
        return productRepository.findById(id)
                .flatMap(productRepository::delete);
    }

    @Override
    public Mono<ProductDto> findById(String id) {
        return productRepository.findById(id)
                .map(mapperProduct::toDto);
    }

    @Override
    public Flux<ProductDto> findAll() {
        return productRepository.findAll()
                .map(mapperProduct::toDto);
    }
}
