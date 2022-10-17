package com.nttdata.bootcamp.accountservice.infrastructure;

import com.nttdata.bootcamp.accountservice.model.SavingsAccount;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface SavingsAccountRepository extends ReactiveMongoRepository<SavingsAccount, String> {
    Mono<SavingsAccount> findByHolderId(String id);
    Mono<SavingsAccount> findByNumber(String number);
}
