package com.nttdata.bootcamp.productservice.application;

import com.nttdata.bootcamp.productservice.application.mappers.MapperProductType;
import com.nttdata.bootcamp.productservice.infrastructure.ProductTypeRepository;
import com.nttdata.bootcamp.productservice.model.dto.ProductTypeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductTypeServiceImpl implements ProductTypeService {
    @Autowired
    MapperProductType mapperProductType;

    @Autowired
    ProductTypeRepository productTypeRepository;

    @Override
    public Mono<ProductTypeDto> create(ProductTypeDto productTypeDto) {
        return mapperProductType.toTypeProduct(productTypeDto)
                .flatMap(productTypeRepository::insert)
                .flatMap(mapperProductType::toDto);
    }

    @Override
    public Mono<ProductTypeDto> update(String id, ProductTypeDto productTypeDto) {
        return productTypeRepository.findById(id)
                .flatMap(typeProduct -> {
                    typeProduct.setName(productTypeDto.getName());
                    return productTypeRepository.save(typeProduct);
                })
                .flatMap(mapperProductType::toDto);
    }

    @Override
    public Mono<Void> delete(String id) {
        return productTypeRepository.findById(id)
                .flatMap(productTypeRepository::delete);
    }

    @Override
    public Mono<ProductTypeDto> findById(String id) {
        return productTypeRepository.findById(id)
                .flatMap(mapperProductType::toDto);
    }

    @Override
    public Flux<ProductTypeDto> findAll() {
        return productTypeRepository.findAll()
                .flatMap(mapperProductType::toDto);
    }
}
