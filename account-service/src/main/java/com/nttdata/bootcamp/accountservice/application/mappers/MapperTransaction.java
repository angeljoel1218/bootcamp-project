package com.nttdata.bootcamp.accountservice.application.mappers;

import com.nttdata.bootcamp.accountservice.model.Transaction;
import com.nttdata.bootcamp.accountservice.model.dto.TransactionDto;
import org.modelmapper.ModelMapper;
import reactor.core.publisher.Mono;

public class MapperTransaction {
    public Mono<TransactionDto> toDto(Transaction transaction) {
        ModelMapper modelMapper = new ModelMapper();
        TransactionDto transactionDto = modelMapper.map(transaction, TransactionDto.class);
        return Mono.just(transactionDto);
    }

    public Mono<Transaction> toTransaction(TransactionDto transactionDto) {
        ModelMapper modelMapper = new ModelMapper();
        Transaction transaction = modelMapper.map(transactionDto, Transaction.class);
        return Mono.just(transaction);
    }
}
