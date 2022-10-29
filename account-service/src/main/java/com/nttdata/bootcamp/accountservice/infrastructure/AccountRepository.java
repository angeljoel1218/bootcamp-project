package com.nttdata.bootcamp.accountservice.infrastructure;

import com.nttdata.bootcamp.accountservice.model.Account;
import com.nttdata.bootcamp.accountservice.model.CurrentAccount;
import com.nttdata.bootcamp.accountservice.model.constant.TypeAccount;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountRepository extends ReactiveMongoRepository<Account, String> {
    Mono<Account> findByNumber(String number);
    Flux<Account> findByHolderId(String id);
}
