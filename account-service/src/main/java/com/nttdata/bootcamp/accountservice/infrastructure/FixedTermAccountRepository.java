package com.nttdata.bootcamp.accountservice.infrastructure;

import com.nttdata.bootcamp.accountservice.model.FixedTermAccount;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface FixedTermAccountRepository extends ReactiveMongoRepository<FixedTermAccount, String> {
    Mono<FixedTermAccount> findByHolderId(String id);
    Mono<Long> countByHolderId(String id);
    Mono<FixedTermAccount> findByNumber(String number);

}
