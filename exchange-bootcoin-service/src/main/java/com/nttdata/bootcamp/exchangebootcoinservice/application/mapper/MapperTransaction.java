package com.nttdata.bootcamp.exchangebootcoinservice.application.mapper;

import com.nttdata.bootcamp.exchangebootcoinservice.domain.dto.TransactionDto;
import com.nttdata.bootcamp.exchangebootcoinservice.domain.model.Transaction;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class MapperTransaction {
    public TransactionDto toDto(Transaction transaction) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(transaction, TransactionDto.class);
    }

    public Transaction toTransaction(TransactionDto transactionDto) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(transactionDto, Transaction.class);
    }
}
