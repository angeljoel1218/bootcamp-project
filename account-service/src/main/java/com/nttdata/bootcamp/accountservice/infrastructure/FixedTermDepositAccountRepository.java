package com.nttdata.bootcamp.accountservice.infrastructure;

import com.nttdata.bootcamp.accountservice.model.FixedTermDepositAccount;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface FixedTermDepositAccountRepository extends ReactiveMongoRepository<FixedTermDepositAccount, String> {
}
