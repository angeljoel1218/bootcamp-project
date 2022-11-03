package com.nttdata.bootcamp.wallettransactionservice.application.mapper;

import com.nttdata.bootcamp.wallettransactionservice.model.Transaction;
import com.nttdata.bootcamp.wallettransactionservice.model.dto.TransactionDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class MapperGeneric {
    public TransactionDto toTransactionDto(Transaction transaction){
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(transaction, TransactionDto.class);
    }
}
