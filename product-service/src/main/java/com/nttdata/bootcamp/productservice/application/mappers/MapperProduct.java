package com.nttdata.bootcamp.productservice.application.mappers;

import com.nttdata.bootcamp.productservice.model.Product;
import com.nttdata.bootcamp.productservice.model.dto.ProductDto;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * Some javadoc.
 * @since 2022
 */
@Slf4j
@Component
public class MapperProduct {
    public ProductDto toDto(Product product) {
      ModelMapper modelMapper = new ModelMapper();
      return modelMapper.map(product, ProductDto.class);

    }

    public Product toProduct(ProductDto productDto) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(productDto, Product.class);
    }
}
