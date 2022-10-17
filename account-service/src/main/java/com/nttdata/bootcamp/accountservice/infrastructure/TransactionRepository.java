package com.nttdata.bootcamp.accountservice.infrastructure;

import com.nttdata.bootcamp.accountservice.model.Transaction;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

import java.util.Date;

public interface TransactionRepository extends ReactiveMongoRepository<Transaction, String> {
    Flux<Transaction> findByAccountId(String id);
    Flux<Transaction> findByDateOfTransactionBetweenAndAccountId(Date start, Date end, String accountId);
}
