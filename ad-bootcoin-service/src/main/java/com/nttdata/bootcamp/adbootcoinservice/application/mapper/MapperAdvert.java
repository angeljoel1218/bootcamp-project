package com.nttdata.bootcamp.adbootcoinservice.application.mapper;

import com.nttdata.bootcamp.adbootcoinservice.domain.dto.AdvertDto;
import com.nttdata.bootcamp.adbootcoinservice.domain.model.Advert;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;



/**
 *
 * @since 2022
 */
@Component
public class MapperAdvert {
    public AdvertDto toDto(Advert advert) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(advert, AdvertDto.class);
    }

    public Advert toAdvert(AdvertDto advertDto) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(advertDto, Advert.class);
    }
}
