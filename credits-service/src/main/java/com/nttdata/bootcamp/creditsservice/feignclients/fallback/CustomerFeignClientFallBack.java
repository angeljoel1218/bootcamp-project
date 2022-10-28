package com.nttdata.bootcamp.creditsservice.feignclients.fallback;

import com.nttdata.bootcamp.creditsservice.feignclients.CustomerFeignClient;
import com.nttdata.bootcamp.creditsservice.feignclients.ProductFeignClient;
import com.nttdata.bootcamp.creditsservice.model.dto.CustomerDto;
import com.nttdata.bootcamp.creditsservice.model.dto.ProductCreditDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Log4j2
@Component
public class CustomerFeignClientFallBack implements CustomerFeignClient {

	@Value("${feign.service.customer.name}")
	private String productService;

	@Override
	public Mono<CustomerDto> findById(String id) {
		log.info("findById customer  not found!" +productService);

		return null;
	}
}
