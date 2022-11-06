package com.nttdata.bootcamp.adbootcoinservice.application.mapper;

import com.nttdata.bootcamp.adbootcoinservice.domain.dto.PayOrderDto;
import com.nttdata.bootcamp.adbootcoinservice.domain.model.PayOrder;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;


/**
 *
 * @since 2022
 */
@Component
public class MapperPayOrder {
    public PayOrderDto toDto(PayOrder payOrder) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(payOrder, PayOrderDto.class);
    }

    public PayOrder toPayOrder(PayOrderDto payOrderDto) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(payOrderDto, PayOrder.class);
    }
}
