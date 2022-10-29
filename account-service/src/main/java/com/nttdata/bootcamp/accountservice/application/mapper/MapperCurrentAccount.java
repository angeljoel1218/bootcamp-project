package com.nttdata.bootcamp.accountservice.application.mapper;

import com.nttdata.bootcamp.accountservice.model.CurrentAccount;
import com.nttdata.bootcamp.accountservice.model.dto.CurrentAccountDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class MapperCurrentAccount {
    public Mono<CurrentAccountDto> toDto(CurrentAccount currentAccount) {
        ModelMapper modelMapper = new ModelMapper();
        CurrentAccountDto currentAccountDto = modelMapper.map(currentAccount, CurrentAccountDto.class);
        return Mono.just(currentAccountDto);
    }

    public Mono<CurrentAccount> toCurrentAccount(CurrentAccountDto currentAccountDto) {
        ModelMapper modelMapper = new ModelMapper();
        CurrentAccount currentAccount = modelMapper.map(currentAccountDto, CurrentAccount.class);
        return Mono.just(currentAccount);
    }
}
