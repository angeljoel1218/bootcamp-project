package com.nttdata.bootcamp.creditsservice.application.mappers;

import com.nttdata.bootcamp.creditsservice.model.CreditDues;
import com.nttdata.bootcamp.creditsservice.model.dto.CreditDuesDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * javadoc.
 * Bank
 * @since 2022
 */
@Component
public class MapperCredit {
  public CreditDuesDto toCreditDto(CreditDues creditDues) {
    ModelMapper modelMapper = new ModelMapper();
    return  modelMapper.map(creditDues, CreditDuesDto.class);
  }

  public CreditDues toCredit(CreditDuesDto  creditDuesDto) {
    ModelMapper modelMapper = new ModelMapper();
    return modelMapper.map(creditDuesDto, CreditDues.class);
  }
}
