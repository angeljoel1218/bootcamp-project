package com.nttdata.bootcamp.accountservice.application.mappers;

import com.nttdata.bootcamp.accountservice.model.Account;
import com.nttdata.bootcamp.accountservice.model.Transaction;
import com.nttdata.bootcamp.accountservice.model.dto.AccountDto;
import com.nttdata.bootcamp.accountservice.model.dto.TransactionDto;
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

    public Mono<Account> toAccount(TransactionDto transactionDto) {
        ModelMapper modelMapper = new ModelMapper();
        Account account = modelMapper.map(transactionDto, Account.class);
        return Mono.just(account);
    }
}
