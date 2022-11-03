package com.nttdata.bootcamp.walletservice.infrastructure;

import com.nttdata.bootcamp.walletservice.model.Wallet;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface WalletRepository extends ReactiveMongoRepository<Wallet, String> {
}
