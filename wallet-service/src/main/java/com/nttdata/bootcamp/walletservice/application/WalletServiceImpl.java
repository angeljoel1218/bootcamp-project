package com.nttdata.bootcamp.walletservice.application;

import com.nttdata.bootcamp.walletservice.infrastructure.WalletRepository;
import com.nttdata.bootcamp.walletservice.model.Wallet;
import com.nttdata.bootcamp.walletservice.model.feignclient.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
public class WalletServiceImpl implements WalletService {

  @Autowired
  WalletRepository walletRepository;

  @Autowired
  CardService cardService;

  @Override
  public Mono<Wallet> create(Wallet wallet) {
    return  Mono.just(wallet).flatMap(walletRepository::insert);
  }

  @Override
  public Mono<Wallet> findByPhone(String phone) {
    return walletRepository.findByPhone(phone);
  }

  @Override
  public Mono<Wallet> addCard(String phone,  String cardNumber, String cardCvv) {
    return cardService.findCardByNumberAndCvv(cardNumber, cardCvv)
      .switchIfEmpty(Mono.error(new InterruptedException("The card not found")))
      .flatMap(c ->
        walletRepository.findByPhone(phone).map(w -> {
          w.setCard(c);
          return  w;
        }).flatMap(walletRepository::save));
  }

  @Override
  public Mono<Wallet> setBalance(String phone, BigDecimal amount) {
    return walletRepository.findByPhone(phone)
      .map(w -> {
        w.setBalance(w.getBalance().add(amount));
        return w;
      }).flatMap(walletRepository::save);
  }


  @Override
  public Flux<Wallet> findAll() {
    return walletRepository.findAll();
  }
}
