package com.nttdata.bootcamp.creditsservice.feignclients.fallback;

import com.nttdata.bootcamp.creditsservice.feignclients.ProductFeignClient;
import com.nttdata.bootcamp.creditsservice.model.Credit;
import com.nttdata.bootcamp.creditsservice.model.dto.ProductCreditDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Log4j2
@Component
public class ProductFeignClientFallBack implements ProductFeignClient {

	@Override
	public Mono<ProductCreditDto> findById(String id) {
		log.info("findById product Credit not found!");
		return null;
	}
}
