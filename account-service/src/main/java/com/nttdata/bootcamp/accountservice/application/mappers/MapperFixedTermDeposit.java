package com.nttdata.bootcamp.accountservice.application.mappers;

import com.nttdata.bootcamp.accountservice.model.FixedTermDepositAccount;
import com.nttdata.bootcamp.accountservice.model.dto.FixedTermDepositAccountDto;
import org.modelmapper.ModelMapper;
import reactor.core.publisher.Mono;

public class MapperFixedTermDeposit {
    public Mono<FixedTermDepositAccountDto> toDto(FixedTermDepositAccount fixedTermDepositAccount) {
        ModelMapper modelMapper = new ModelMapper();
        FixedTermDepositAccountDto fixedTermDepositAccountDto = modelMapper.map(fixedTermDepositAccount, FixedTermDepositAccountDto.class);
        return Mono.just(fixedTermDepositAccountDto);
    }

    public Mono<FixedTermDepositAccount> toProductAccount(FixedTermDepositAccountDto fixedTermDepositAccountDto) {
        ModelMapper modelMapper = new ModelMapper();
        FixedTermDepositAccount fixedTermDepositAccount = modelMapper.map(fixedTermDepositAccountDto, FixedTermDepositAccount.class);
        return Mono.just(fixedTermDepositAccount);
    }
}
