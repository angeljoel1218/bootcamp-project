package com.nttdata.bootcamp.productservice.application.mappers;

import com.nttdata.bootcamp.productservice.model.ProductType;
import com.nttdata.bootcamp.productservice.model.dto.ProductTypeDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
@Component
public class MapperProductType {
    public Mono<ProductTypeDto> toDto(ProductType productType) {
        ModelMapper modelMapper = new ModelMapper();
        ProductTypeDto productTypeDto = modelMapper.map(productType, ProductTypeDto.class);
        return Mono.just(productTypeDto);
    }

    public Mono<ProductType> toTypeProduct(ProductTypeDto productTypeDto) {
        ModelMapper modelMapper = new ModelMapper();
        ProductType productType = modelMapper.map(productTypeDto, ProductType.class);
        return Mono.just(productType);
    }
}
