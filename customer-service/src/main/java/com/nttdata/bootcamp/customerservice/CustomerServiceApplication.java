package com.nttdata.bootcamp.customerservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * Some javadoc.
 *
 * @author Alex Bejarano
 * @since 2022
 */
@SpringBootApplication
@EnableEurekaClient
public class CustomerServiceApplication {
  public static void main(String[] args) {
    SpringApplication.run(CustomerServiceApplication.class, args);
  }
}
