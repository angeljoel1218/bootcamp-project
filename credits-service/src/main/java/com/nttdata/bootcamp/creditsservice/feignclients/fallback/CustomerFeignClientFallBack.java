package com.nttdata.bootcamp.creditsservice.feignclients.fallback;

import com.nttdata.bootcamp.creditsservice.feignclients.CustomerFeignClient;
import com.nttdata.bootcamp.creditsservice.model.dto.CustomerDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * javadoc.
 * Bank
 * @since 2022
 */

@Log4j2
@Component
public class CustomerFeignClientFallBack implements CustomerFeignClient {
  @Override
  public Mono<CustomerDto> findById(String id) {
    log.info("findById customer  not found!");
    return null;
  }
}
