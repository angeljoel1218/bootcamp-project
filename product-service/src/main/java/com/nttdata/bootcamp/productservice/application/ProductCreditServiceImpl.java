package com.nttdata.bootcamp.productservice.application;

import com.nttdata.bootcamp.productservice.application.mappers.MapperProductCredit;
import com.nttdata.bootcamp.productservice.infrastructure.ProductCreditRepository;
import com.nttdata.bootcamp.productservice.model.ProductCredit;
import com.nttdata.bootcamp.productservice.model.dto.ProductCreditDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductCreditServiceImpl implements ProductCreditService {
    @Autowired
    MapperProductCredit mapperProductCredit;

    @Autowired
    ProductCreditRepository productCreditRepository;

    @Override
    public Mono<ProductCreditDto> create(ProductCreditDto credit) {
        Mono<ProductCredit> productCreditMono = mapperProductCredit.toProductCredit(credit);
        Mono<ProductCredit> productCredit = productCreditMono.flatMap(productCreditRepository::insert);
        return productCredit.flatMap(mapperProductCredit::toDto);
    }

    @Override
    public Mono<ProductCreditDto> update(String id, ProductCreditDto productCreditDto) {
        Mono<ProductCredit> foundProductCredit = productCreditRepository.findById(id);
        Mono<ProductCredit> upProductCredit = foundProductCredit.flatMap(c -> {
            c.setName(productCreditDto.getName());
            c.setMaxNumber(productCreditDto.getMaxNumber());
            c.setType(productCreditDto.getType());
            c.setCoin(productCreditDto.getCoin());
            return productCreditRepository.save(c);
        });
        return upProductCredit.flatMap(mapperProductCredit::toDto);
    }

    @Override
    public Mono<Void> delete(String id) {
        return productCreditRepository.findById(id)
                .flatMap(productCreditRepository::delete);
    }

    @Override
    public Mono<ProductCreditDto> findById(String id) {
        return productCreditRepository.findById(id)
                .flatMap(mapperProductCredit::toDto);
    }

    @Override
    public Flux<ProductCreditDto> findAll() {
        return productCreditRepository.findAll()
                .flatMap(mapperProductCredit::toDto);
    }

}
