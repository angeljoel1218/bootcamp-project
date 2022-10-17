package com.nttdata.bootcamp.accountservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactivefeign.spring.config.EnableReactiveFeignClients;

@SpringBootApplication
@EnableReactiveFeignClients
public class AccountServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccountServiceApplication.class, args);
	}

}
