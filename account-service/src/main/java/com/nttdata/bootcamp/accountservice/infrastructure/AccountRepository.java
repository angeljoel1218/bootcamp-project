package com.nttdata.bootcamp.accountservice.infrastructure;

import com.nttdata.bootcamp.accountservice.model.Account;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *
 * @since 2022
 */
public interface AccountRepository extends ReactiveMongoRepository<Account, String> {
    Mono<Account> findByNumber(String number);
    Flux<Account> findByHolderId(String holderId);
}
