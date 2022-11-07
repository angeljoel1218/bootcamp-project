package com.nttdata.bootcamp.reportservice.feignclients.fallback;

import com.nttdata.bootcamp.reportservice.feignclients.AccountFeignClient;
import com.nttdata.bootcamp.reportservice.feignclients.CustomerFeignClient;
import com.nttdata.bootcamp.reportservice.model.dto.AccountDto;
import com.nttdata.bootcamp.reportservice.model.dto.CustomerDto;
import com.nttdata.bootcamp.reportservice.model.dto.TransactionDto;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *
 * @since 2022
 */
@Log4j2
public class CustomerFeignClientFallBack implements CustomerFeignClient {
    @Override
    public Mono<CustomerDto> findCustomerById(String id) {
        log.info("findCustomerById service no available  {}", id);
        return Mono.justOrEmpty(null);
    }
}
