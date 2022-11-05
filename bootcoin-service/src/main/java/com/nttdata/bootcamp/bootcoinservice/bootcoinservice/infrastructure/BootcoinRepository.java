package com.nttdata.bootcamp.bootcoinservice.bootcoinservice.infrastructure;

import com.nttdata.bootcamp.bootcoinservice.bootcoinservice.model.Bootcoin;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface BootcoinRepository extends ReactiveMongoRepository<Bootcoin, String> {

  Mono<Bootcoin> findByPhone(String phone);

}
