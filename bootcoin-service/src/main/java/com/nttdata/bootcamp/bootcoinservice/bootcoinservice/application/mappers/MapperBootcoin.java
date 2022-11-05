package com.nttdata.bootcamp.bootcoinservice.bootcoinservice.application.mappers;


import com.nttdata.bootcamp.bootcoinservice.bootcoinservice.model.Bootcoin;
import com.nttdata.bootcamp.bootcoinservice.bootcoinservice.model.dto.BootcoinDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;


/**
 * Some javadoc.
 *
 * @since 2022
 */

@Component
public class MapperBootcoin {

  public BootcoinDto toDto(Bootcoin bootcoin) {
    ModelMapper modelMapper = new ModelMapper();
    return modelMapper.map(bootcoin, BootcoinDto.class);
  }

  public Bootcoin toBootCoin(BootcoinDto  bootcoinDto) {
    ModelMapper modelMapper = new ModelMapper();
    return modelMapper.map(bootcoinDto, Bootcoin.class);
  }
}
