package com.nttdata.bootcamp.reportservice.model;

import com.nttdata.bootcamp.reportservice.model.dto.CreditCardDto;
import com.nttdata.bootcamp.reportservice.model.dto.CreditDto;
import com.nttdata.bootcamp.reportservice.model.dto.ProductDto;
import lombok.Data;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 *
 * @since 2022
 */
@Data
public class ProductConsumer {

    private ProductDto productDto;

    private List<?> products;

}
