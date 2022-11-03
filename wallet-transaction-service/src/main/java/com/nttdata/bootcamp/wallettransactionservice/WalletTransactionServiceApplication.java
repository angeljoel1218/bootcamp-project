package com.nttdata.bootcamp.wallettransactionservice;

import com.nttdata.bootcamp.wallettransactionservice.model.LastTransaction;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
@EnableEurekaClient
public class WalletTransactionServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(WalletTransactionServiceApplication.class, args);
	}

	@Bean
	@LoadBalanced
	public WebClient loadBalancedWebClientBuilder() {
		return WebClient.builder()
				.baseUrl("${webclient.baseurl}")
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.build();
	}

	@Bean
	public ReactiveRedisTemplate<String, LastTransaction> reactiveJsonPostRedisTemplate(
			ReactiveRedisConnectionFactory connectionFactory) {
		RedisSerializationContext<String, LastTransaction> serializationContext =
				RedisSerializationContext
						.<String, LastTransaction>newSerializationContext(new StringRedisSerializer())
						.hashKey(new StringRedisSerializer())
						.hashValue(new Jackson2JsonRedisSerializer<>(LastTransaction.class))
						.build();
		return new ReactiveRedisTemplate<>(connectionFactory, serializationContext);
	}
}
