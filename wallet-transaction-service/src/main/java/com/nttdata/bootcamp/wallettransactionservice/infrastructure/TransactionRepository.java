package com.nttdata.bootcamp.wallettransactionservice.infrastructure;

import com.nttdata.bootcamp.wallettransactionservice.model.Transaction;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface TransactionRepository extends ReactiveMongoRepository<Transaction, String> {
}
