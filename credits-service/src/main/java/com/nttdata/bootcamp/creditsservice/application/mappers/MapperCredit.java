package com.nttdata.bootcamp.creditsservice.application.mappers;

import com.nttdata.bootcamp.creditsservice.model.CreditDues;
import com.nttdata.bootcamp.creditsservice.model.dto.CreditDuesDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class MapperCredit {
    public CreditDuesDto toCreditDto(CreditDues creditDues) {
        ModelMapper modelMapper = new ModelMapper();
        CreditDuesDto creditDuesDto = modelMapper.map(creditDues, CreditDuesDto.class);

        return creditDuesDto;
    }

    public CreditDues toCredit(CreditDuesDto  creditDuesDto) {
        ModelMapper modelMapper = new ModelMapper();
        CreditDues creditDues = modelMapper.map(creditDuesDto, CreditDues.class);
        return creditDues;
    }
}
