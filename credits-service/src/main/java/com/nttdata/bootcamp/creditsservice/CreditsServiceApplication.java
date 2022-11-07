package com.nttdata.bootcamp.creditsservice;

import com.nttdata.bootcamp.creditsservice.application.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import reactivefeign.spring.config.EnableReactiveFeignClients;


/**
 *
 * @since 2022
 */
@Slf4j
@SpringBootApplication
@EnableReactiveFeignClients
@EnableEurekaClient
public class CreditsServiceApplication {
  public static void main(String[] args){
    SpringApplication.run(CreditsServiceApplication.class, args);
  }
}
