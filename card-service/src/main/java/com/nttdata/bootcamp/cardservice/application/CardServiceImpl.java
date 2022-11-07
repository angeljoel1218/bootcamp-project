package com.nttdata.bootcamp.cardservice.application;

import com.nttdata.bootcamp.cardservice.application.exception.CardException;
import com.nttdata.bootcamp.cardservice.application.mapper.MapperClass;
import com.nttdata.bootcamp.cardservice.infrastructure.CardMovementRepository;
import com.nttdata.bootcamp.cardservice.infrastructure.CardRepository;
import com.nttdata.bootcamp.cardservice.infrastructure.webclient.AccountService;
import com.nttdata.bootcamp.cardservice.model.BankAccount;
import com.nttdata.bootcamp.cardservice.model.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;

/**
 *
 * @since 2022
 */
@Service
public class CardServiceImpl implements CardService{
  @Autowired
  CardRepository cardRepository;
  @Autowired
  CardMovementRepository cardMovementRepository;
  @Autowired
  AccountService accountService;
  @Autowired
  MapperClass mapperClass;
  @Override
  public Mono<CardDto> registerCard(CardDto cardDto) {
    return accountService.findByHolderId(cardDto.getHolderId())
        .collectList()
        .switchIfEmpty(Mono.error(new CardException("The customer does not have bank accounts")))
        .doOnNext(accounts -> {
          if (Objects.nonNull(cardDto.getAccounts())) {
            cardDto.getAccounts()
                .forEach(bankAccount -> {
                  if (Objects.isNull(accounts.stream()
                      .filter(accountDto -> bankAccount.getAccountNumber()
                        .equals(accountDto.getNumber()))
                      .findFirst()
                      .orElse(null))) {
                    throw new CardException("Account number does not exist for the user "
                      + bankAccount.getAccountNumber());
                  }
                });
          }
        })
        .then(Mono.just(cardDto).map(mapperClass::toCard)
            .flatMap(cardRepository::insert)
            .map(mapperClass::toDto));
  }

  @Override
  public Mono<BankAccountDto> attachAccount(String cardId, AttachAccountDto attachAccountDto) {
    return cardRepository.findById(cardId)
        .switchIfEmpty(Mono.error(new CardException("The card not found")))
        .flatMap(card -> {
          List<BankAccount> accounts = card.getAccounts();
          if (Objects.isNull(accounts)) {
            accounts = new ArrayList<>();
          }
          BankAccount bankAccount = new BankAccount();
          bankAccount.setAccountNumber(attachAccountDto.getAccountNumber());
          bankAccount.setOrder(accounts.size() + 1);
          bankAccount.setCreatedAt(new Date());
          bankAccount.setIsDefault(bankAccount.getOrder() == 1);
          card.getAccounts().add(bankAccount);
          return cardRepository.save(card)
              .thenReturn(mapperClass.toBankAccountDto(bankAccount));
        });
  }

  @Override
  public Flux<CardMovementDto> listMovements(String cardId) {
    return cardMovementRepository.findByCardId(cardId)
        .map(mapperClass::toCardMovementDto);
  }

  @Override
  public Mono<AccountDto> mainAccountBalance(String cardNumber) {
    return cardRepository.findByNumber(cardNumber)
        .switchIfEmpty(Mono.error(new CardException("The number card does not exist")))
        .flatMap(card -> {
          String number = card.getAccounts()
              .stream()
              .filter(BankAccount::getIsDefault)
              .map(BankAccount::getAccountNumber)
              .findFirst()
              .orElse(null);
          if (Objects.isNull(number)) {
            throw new CardException("The card has no linked accounts");
          }
          return accountService.findByNumber(number);
        });
  }
}
