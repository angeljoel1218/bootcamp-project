package com.nttdata.bootcamp.accountservice.infrastructure;

import com.nttdata.bootcamp.accountservice.model.SavingsAccount;
import com.nttdata.bootcamp.accountservice.model.constant.TypeAccount;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;
public interface SavingsAccountRepository extends ReactiveMongoRepository<SavingsAccount, String> {
    Mono<SavingsAccount> findByHolderIdAndTypeAccount(String id, TypeAccount typeAccount);
    Mono<Long> countByHolderId(String id);
    Mono<SavingsAccount> findByNumberAndTypeAccount(String number, TypeAccount typeAccount);
}
