package com.nttdata.bootcamp.masterdatabootcoinservice;

import com.nttdata.bootcamp.masterdatabootcoinservice.domain.ExchangeRate;
import com.nttdata.bootcamp.masterdatabootcoinservice.domain.MethodPayment;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 *
 * @since 2022
 */

@SpringBootApplication
@EnableEurekaClient
public class MasterDataBootcoinServiceApplication {
  public static void main(String[] args){
    SpringApplication.run(MasterDataBootcoinServiceApplication.class, args);
  }

  @Bean
  public ReactiveRedisTemplate<String, ExchangeRate> reactiveJsonPostRedisTemplate(
    ReactiveRedisConnectionFactory connectionFactory) {
    RedisSerializationContext<String, ExchangeRate> serializationContext =
      RedisSerializationContext
          .<String, ExchangeRate>newSerializationContext(new StringRedisSerializer())
          .hashKey(new StringRedisSerializer())
          .hashValue(new Jackson2JsonRedisSerializer<>(ExchangeRate.class))
          .build();
    return new ReactiveRedisTemplate<>(connectionFactory, serializationContext);
  }

  @Bean
  public ReactiveRedisTemplate<String, MethodPayment> reactiveJsonPostRedisTemplate2(
    ReactiveRedisConnectionFactory connectionFactory) {
    RedisSerializationContext<String, MethodPayment> serializationContext =
      RedisSerializationContext
          .<String, MethodPayment>newSerializationContext(new StringRedisSerializer())
          .hashKey(new StringRedisSerializer())
          .hashValue(new Jackson2JsonRedisSerializer<>(MethodPayment.class))
          .build();
    return new ReactiveRedisTemplate<>(connectionFactory, serializationContext);
  }
}
