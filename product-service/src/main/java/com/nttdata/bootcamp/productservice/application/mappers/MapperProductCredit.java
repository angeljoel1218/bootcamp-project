package com.nttdata.bootcamp.productservice.application.mappers;

import com.nttdata.bootcamp.productservice.model.ProductCredit;
import com.nttdata.bootcamp.productservice.model.dto.ProductCreditDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class MapperProductCredit {
    public Mono<ProductCreditDto> toDto(ProductCredit productCredit) {
        ModelMapper modelMapper = new ModelMapper();
        ProductCreditDto productCreditDto = modelMapper.map(productCredit, ProductCreditDto.class);
        return Mono.just(productCreditDto);
    }

    public Mono<ProductCredit> toProductCredit(ProductCreditDto productCreditDto) {
        ModelMapper modelMapper = new ModelMapper();
        ProductCredit productCredit= modelMapper.map(productCreditDto, ProductCredit.class);
        return Mono.just(productCredit);
    }
}
