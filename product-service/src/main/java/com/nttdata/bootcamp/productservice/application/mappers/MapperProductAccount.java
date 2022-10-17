package com.nttdata.bootcamp.productservice.application.mappers;

import com.nttdata.bootcamp.productservice.model.dto.ProductAccountDto;
import com.nttdata.bootcamp.productservice.model.ProductAccount;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class MapperProductAccount {
    public Mono<ProductAccountDto> toDto(ProductAccount productAccount) {
        ModelMapper modelMapper = new ModelMapper();
        ProductAccountDto productAccountDto = modelMapper.map(productAccount, ProductAccountDto.class);
        return Mono.just(productAccountDto);
    }

    public Mono<ProductAccount> toProductAccount(ProductAccountDto productAccountDto) {
        ModelMapper modelMapper = new ModelMapper();
        ProductAccount productAccount = modelMapper.map(productAccountDto, ProductAccount.class);
        return Mono.just(productAccount);
    }
}
