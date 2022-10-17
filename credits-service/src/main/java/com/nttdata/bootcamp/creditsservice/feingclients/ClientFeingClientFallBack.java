package com.nttdata.bootcamp.creditsservice.feingclients;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Log4j2
@Component
public class ClientFeingClientFallBack implements  ClientFeingClient{
    @Override
    public Mono<Object> findById(String id) {
        // TODO Auto-generated method stub
        // https://arnoldgalovics.com/feign-fallback/

        log.info("CustomerFeignClientFallBack[/" + id + "]");
        return null;
    }
}
