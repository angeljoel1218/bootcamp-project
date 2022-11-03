package com.nttdata.bootcamp.walletservice.application;

import com.nttdata.bootcamp.walletservice.model.Wallet;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface WalletService {

  Mono<Wallet> create(Wallet wallet);

  Mono<Wallet> update(String id, Wallet wallet);

  Mono<Void> delete(String id);

  Mono<Wallet> findById(String id);

  Flux<Wallet> findAll();

  Mono<Wallet> findByPhone(String phone);

  Mono<Wallet> addCard(String phone, String cardId);

}
