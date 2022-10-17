package com.nttdata.bootcamp.accountservice.infrastructure;

import com.nttdata.bootcamp.accountservice.model.FixedTermDepositAccount;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface FixedTermDepositAccountRepository extends ReactiveMongoRepository<FixedTermDepositAccount, String> {
    Mono<FixedTermDepositAccount> findByHolderId(String id);
    Mono<FixedTermDepositAccount> findByNumber(String number);
}
