package com.nttdata.bootcamp.walletservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactivefeign.spring.config.EnableReactiveFeignClients;

/**
 *
 * @since 2022
 */

@SpringBootApplication
@EnableReactiveFeignClients
@EnableEurekaClient
public class WalletServiceApplication {
  public static void main(String[] args) {
    SpringApplication.run(WalletServiceApplication.class, args);
  }

  @Bean
  @LoadBalanced
  public WebClient loadBalancedWebClientBuilder() {
    return WebClient.builder()
      .baseUrl("${webclient.baseurl}")
      .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
      .build();
  }
}
