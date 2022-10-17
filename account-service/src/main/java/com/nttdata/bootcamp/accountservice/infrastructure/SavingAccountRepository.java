package com.nttdata.bootcamp.accountservice.infrastructure;

import com.nttdata.bootcamp.accountservice.model.SavingAccount;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface SavingAccountRepository extends ReactiveMongoRepository<SavingAccount, String> {
}
