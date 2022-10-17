package com.nttdata.bootcamp.accountservice.application.mappers;

import com.nttdata.bootcamp.accountservice.model.SavingAccount;
import com.nttdata.bootcamp.accountservice.model.dto.SavingAccountDto;
import org.modelmapper.ModelMapper;
import reactor.core.publisher.Mono;

public class MapperSavingAccount {
    public Mono<SavingAccountDto> toDto(SavingAccount savingAccount) {
        ModelMapper modelMapper = new ModelMapper();
        SavingAccountDto savingAccountDto = modelMapper.map(savingAccount, SavingAccountDto.class);
        return Mono.just(savingAccountDto);
    }

    public Mono<SavingAccount> toSavingAccount(SavingAccountDto savingAccountDto) {
        ModelMapper modelMapper = new ModelMapper();
        SavingAccount savingAccount = modelMapper.map(savingAccountDto, SavingAccount.class);
        return Mono.just(savingAccount);
    }
}
