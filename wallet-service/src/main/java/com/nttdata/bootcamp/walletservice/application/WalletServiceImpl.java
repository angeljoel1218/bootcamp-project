package com.nttdata.bootcamp.walletservice.application;

import com.nttdata.bootcamp.walletservice.application.mappers.MapperWallet;
import com.nttdata.bootcamp.walletservice.infrastructure.WalletRepository;
import com.nttdata.bootcamp.walletservice.model.dto.WalletDto;
import com.nttdata.bootcamp.walletservice.infrastructure.feignclient.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *
 * @since 2022
 */

@Service
public class WalletServiceImpl implements WalletService {

  @Autowired
  WalletRepository walletRepository;

  @Autowired
  CardService cardService;

  @Autowired
  MapperWallet mapperWallet;

  @Override
  public Mono<WalletDto> create(WalletDto walletDto) {
    return  Mono.just(walletDto).map(mapperWallet::toWallet)
      .flatMap(t->{
        t.setBalance(BigDecimal.ZERO);
        t.setCreateAt(LocalDate.now());
        return walletRepository.insert(t);
    }).map(mapperWallet::toDto);
  }

  @Override
  public Mono<WalletDto> findByPhone(String phone) {
    return walletRepository.findByPhone(phone).map(mapperWallet::toDto);
  }

  @Override
  public Mono<WalletDto> addCard(String phone,  String cardNumber, String cardCvv) {
    return cardService.findCardByNumberAndCvv(cardNumber, cardCvv)
      .switchIfEmpty(Mono.error(new InterruptedException("The card not found")))
      .flatMap(c ->
        walletRepository.findByPhone(phone).map(w -> {
          w.setCard(c);
          return  w;
        }).flatMap(walletRepository::save)).map(mapperWallet::toDto);
  }

  @Override
  public Mono<WalletDto> setBalance(String phone, BigDecimal amount) {
    return walletRepository.findByPhone(phone)
      .flatMap(w -> {
        w.setBalance(w.getBalance().add(amount));
        return walletRepository.save(w);
      }).map(mapperWallet::toDto);
  }


  @Override
  public Flux<WalletDto> findAll() {
    return walletRepository.findAll().map(mapperWallet::toDto);
  }
}
