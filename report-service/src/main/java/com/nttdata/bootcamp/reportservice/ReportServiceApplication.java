package com.nttdata.bootcamp.reportservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import reactivefeign.spring.config.EnableReactiveFeignClients;

/**
 *
 * @since 2022
 */
@SpringBootApplication
@EnableReactiveFeignClients
@EnableEurekaClient
public class ReportServiceApplication {
  public static void main(String[] args) {
    SpringApplication.run(ReportServiceApplication.class, args);
  }
}
