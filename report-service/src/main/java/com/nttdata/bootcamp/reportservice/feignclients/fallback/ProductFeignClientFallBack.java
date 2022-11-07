package com.nttdata.bootcamp.reportservice.feignclients.fallback;

import com.nttdata.bootcamp.reportservice.feignclients.CustomerFeignClient;
import com.nttdata.bootcamp.reportservice.feignclients.ProductFeignClient;
import com.nttdata.bootcamp.reportservice.model.dto.CustomerDto;
import com.nttdata.bootcamp.reportservice.model.dto.ProductDto;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;


/**
 *
 * @since 2022
 */
@Log4j2
public class ProductFeignClientFallBack implements ProductFeignClient {

    @Override
    public Mono<ProductDto> findById(String id) {
        log.info("findById service no available  id={}", id);
        return null;
    }
}
