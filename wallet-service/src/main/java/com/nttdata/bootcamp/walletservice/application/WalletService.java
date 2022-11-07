package com.nttdata.bootcamp.walletservice.application;

import com.nttdata.bootcamp.walletservice.model.dto.WalletDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

/**
 *
 * @since 2022
 */

public interface WalletService {

  Mono<WalletDto> create(WalletDto walletDto);

  Mono<WalletDto> findByPhone(String phone);

  Mono<WalletDto> addCard(String phone, String cardNumber, String cardCvv);

  Mono<WalletDto> setBalance(String phone, BigDecimal amount);

  Flux<WalletDto> findAll();



}
