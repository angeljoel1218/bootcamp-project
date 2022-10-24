package com.nttdata.bootcamp.productservice.application.mappers;

import com.nttdata.bootcamp.productservice.model.Product;
import com.nttdata.bootcamp.productservice.model.dto.ProductDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
@Component
public class MapperProduct {
    public Mono<ProductDto> toDto(Product product) {
        ModelMapper modelMapper = new ModelMapper();
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        return Mono.just(productDto);
    }

    public Mono<Product> toProduct(ProductDto productDto) {
        ModelMapper modelMapper = new ModelMapper();
        Product product = modelMapper.map(productDto, Product.class);
        return Mono.just(product);
    }
}
