package com.nttdata.bootcamp.walletservice.application;

import com.nttdata.bootcamp.walletservice.model.Wallet;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface WalletService {

  Mono<Wallet> create(Wallet wallet);

  Mono<Wallet> findByPhone(String phone);

  Mono<Wallet> addCard(String phone, String cardNumber,String cardCvv);

  Mono<Wallet> setBalance(String phone, BigDecimal amount);

  Flux<Wallet> findAll();



}
