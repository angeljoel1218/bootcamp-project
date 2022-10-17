package com.nttdata.bootcamp.accountservice.application.mappers;

import com.nttdata.bootcamp.accountservice.model.SavingsAccount;
import com.nttdata.bootcamp.accountservice.model.dto.SavingsAccountDto;
import org.modelmapper.ModelMapper;
import reactor.core.publisher.Mono;

public class MapperSavingsAccount {
    public Mono<SavingsAccountDto> toDto(SavingsAccount savingsAccount) {
        ModelMapper modelMapper = new ModelMapper();
        SavingsAccountDto savingsAccountDto = modelMapper.map(savingsAccount, SavingsAccountDto.class);
        return Mono.just(savingsAccountDto);
    }

    public Mono<SavingsAccount> toSavingsAccount(SavingsAccountDto savingsAccountDto) {
        ModelMapper modelMapper = new ModelMapper();
        SavingsAccount savingsAccount = modelMapper.map(savingsAccountDto, SavingsAccount.class);
        return Mono.just(savingsAccount);
    }
}
