package com.nttdata.bootcamp.accountservice.infrastructure;

import com.nttdata.bootcamp.accountservice.model.CurrentAccount;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CurrentAccountRepository extends ReactiveMongoRepository<CurrentAccount, String> {
}
