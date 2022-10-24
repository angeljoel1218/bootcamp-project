package com.nttdata.bootcamp.productservice.application;

import com.nttdata.bootcamp.productservice.application.mappers.MapperProduct;
import com.nttdata.bootcamp.productservice.application.mappers.MapperProductType;
import com.nttdata.bootcamp.productservice.infrastructure.ProductRepository;
import com.nttdata.bootcamp.productservice.model.dto.ProductDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductServiceImpl implements ProductService{
    @Autowired
    MapperProduct mapperProduct;

    @Autowired
    MapperProductType mapperProductType;

    @Autowired
    ProductRepository productRepository;

    @Override
    public Mono<ProductDto> create(ProductDto productDto) {
        return mapperProduct.toProduct(productDto)
                .flatMap(productRepository::insert)
                .flatMap(mapperProduct::toDto);
    }

    @Override
    public Mono<ProductDto> update(String id, ProductDto productDto) {
        return productRepository.findById(id)
                .flatMap(product -> {
                    product.setCode(productDto.getCode());
                    product.setName(productDto.getName());
                    product.setMaintenance(productDto.getMaintenance());
                    product.setMaxMovements(productDto.getMaxMovements());
                    product.setMaxNumberCredits(productDto.getMaxNumberCredits());
                    product.setCommissionAmount(productDto.getCommissionAmount());
                    product.setOpeningAmount(productDto.getOpeningAmount());
                    product.setMinFixedAmount(productDto.getMinFixedAmount());
                    product.setCategory(productDto.getCategory());
                    product.setProductTypeId(productDto.getProductTypeId());
                    return productRepository.save(product);
                })
                .flatMap(mapperProduct::toDto);
    }

    @Override
    public Mono<Void> delete(String id) {
        return productRepository.findById(id)
                .flatMap(productRepository::delete);
    }

    @Override
    public Mono<ProductDto> findById(String id) {
        return productRepository.findById(id)
                .flatMap(mapperProduct::toDto);
    }

    @Override
    public Flux<ProductDto> findAll() {
        return productRepository.findAll()
                .flatMap(mapperProduct::toDto);
    }
}
