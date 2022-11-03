package com.nttdata.bootcamp.transactionwalletservice.infrastructure;

import com.nttdata.bootcamp.transactionwalletservice.model.Transaction;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface TransactionRepository extends ReactiveMongoRepository<Transaction, String> {
}
