package com.nttdata.bootcamp.accountservice.application.mappers;

import com.nttdata.bootcamp.accountservice.model.FixedTermAccount;
import com.nttdata.bootcamp.accountservice.model.dto.FixedTermAccountDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class MapperFixedTerm {
    public Mono<FixedTermAccountDto> toDto(FixedTermAccount fixedTermAccount) {
        ModelMapper modelMapper = new ModelMapper();
        FixedTermAccountDto fixedTermAccountDto = modelMapper.map(fixedTermAccount, FixedTermAccountDto.class);
        return Mono.just(fixedTermAccountDto);
    }

    public Mono<FixedTermAccount> toProductAccount(FixedTermAccountDto fixedTermAccountDto) {
        ModelMapper modelMapper = new ModelMapper();
        FixedTermAccount fixedTermAccount = modelMapper.map(fixedTermAccountDto, FixedTermAccount.class);
        return Mono.just(fixedTermAccount);
    }
}
