package com.nttdata.bootcamp.exchangebootcoinservice.application.mapper;

import com.nttdata.bootcamp.exchangebootcoinservice.domain.dto.ConfigPaymentMethodDto;
import com.nttdata.bootcamp.exchangebootcoinservice.domain.model.ConfigPaymentMethod;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class MapperConfigPaymentMethod {
    public ConfigPaymentMethodDto toDto(ConfigPaymentMethod configPaymentMethod) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(configPaymentMethod, ConfigPaymentMethodDto.class);
    }

    public ConfigPaymentMethod toConfigPaymentMethod(ConfigPaymentMethodDto advertDto) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(advertDto, ConfigPaymentMethod.class);
    }
}
