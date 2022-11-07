package com.nttdata.bootcamp.walletservice.infrastructure;

import com.nttdata.bootcamp.walletservice.model.Wallet;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

/**
 *
 * @since 2022
 */

public interface WalletRepository extends ReactiveMongoRepository<Wallet, String> {

  Mono<Wallet> findByPhone(String phone);
}
