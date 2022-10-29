package com.nttdata.bootcamp.accountservice.application.mapper;

import com.nttdata.bootcamp.accountservice.model.Account;
import com.nttdata.bootcamp.accountservice.model.dto.AccountDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class MapperAccount {
    public Mono<AccountDto> toDto(Account account) {
        ModelMapper modelMapper = new ModelMapper();
        AccountDto accountDto = modelMapper.map(account, AccountDto.class);
        return Mono.just(accountDto);
    }

    public Mono<Account> toAccount(AccountDto accountDto) {
        ModelMapper modelMapper = new ModelMapper();
        Account account = modelMapper.map(accountDto, Account.class);
        return Mono.just(account);
    }
}
