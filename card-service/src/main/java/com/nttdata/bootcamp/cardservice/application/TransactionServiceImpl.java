package com.nttdata.bootcamp.cardservice.application;

import com.nttdata.bootcamp.cardservice.application.exception.CardException;
import com.nttdata.bootcamp.cardservice.application.exception.InsufficientBalanceException;
import com.nttdata.bootcamp.cardservice.application.mapper.MapperClass;
import com.nttdata.bootcamp.cardservice.infrastructure.CardMovementRepository;
import com.nttdata.bootcamp.cardservice.infrastructure.CardRepository;
import com.nttdata.bootcamp.cardservice.infrastructure.webclient.AccountService;
import com.nttdata.bootcamp.cardservice.infrastructure.webclient.CreditClient;
import com.nttdata.bootcamp.cardservice.model.BankAccount;
import com.nttdata.bootcamp.cardservice.model.Card;
import com.nttdata.bootcamp.cardservice.model.constant.TypeCredit;
import com.nttdata.bootcamp.cardservice.model.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 *
 * @since 2022
 */
@Service
public class TransactionServiceImpl implements TransactionService{
   @Autowired
   CardRepository cardRepository;
   @Autowired
   CardMovementRepository cardMovementRepository;
   @Autowired
   AccountService accountService;
   @Autowired
   CreditClient creditClient;
   @Autowired
   MapperClass mapperClass;

   @Override
   public Mono<CardMovementDto> payment(PaymentDto paymentDto) {
      return cardRepository.findByNumber(paymentDto.getCardNumber())
            .switchIfEmpty(Mono.error(new CardException("The number card does not exist")))
            .doOnNext(card -> {
               if (!paymentDto.getTypeCredit().equals(TypeCredit.CREDIT)
                     && !paymentDto.getTypeCredit().equals(TypeCredit.CREDIT_CARD))
               {
                  throw new CardException("Type credit not exist");
               }
            })
            .flatMap(card -> {
               WithdrawDto withdrawDto = mapperClass.toWithdrawDto(paymentDto);
               return this.execTransaction(withdrawDto, card, 1)
                     .flatMap(transactionDto -> this.saveCardMovement(transactionDto,
                       withdrawDto, card.getId()));
            })
            .flatMap(cardMovementDto -> {
               if (paymentDto.getTypeCredit().equals(TypeCredit.CREDIT)) {
                  CreditDuesRequestDto creditDuesRequestDto =
                    mapperClass.toCreditDuesRequestDto(paymentDto);
                  return creditClient.paymentCredit(creditDuesRequestDto)
                        .map(s -> cardMovementDto);
               }
               TransactionCreditDto transactionCreditDto =
                 mapperClass.toTransactionCreditDto(paymentDto);

               transactionCreditDto.setDescription(paymentDto.getDetail());

               return creditClient.paymentCreditCard(transactionCreditDto)
                     .map(creditDuesDto -> cardMovementDto);
            });
   }

   @Override
   public Mono<CardMovementDto> withdraw(WithdrawDto withdrawDto) {
      return cardRepository.findByNumber(withdrawDto.getCardNumber())
            .switchIfEmpty(Mono.error(new CardException("The number card does not exist")))
            .flatMap(card -> this.execTransaction(withdrawDto, card, 1)
                  .flatMap(transactionDto -> this.saveCardMovement(transactionDto,
                    withdrawDto, card.getId())));
   }

  @Override
  public Mono<List<CardMovementDto>> findLastByCardIdOrderByOperationDateDesc(String cardId,
                                                                              Integer top) {
    return cardMovementRepository.findByCardIdOrderByOperationDateDesc(cardId)
      .map(mapperClass::toCardMovementDto)
      .collectList().map(list -> {
        return list.stream().limit(top).collect(Collectors.toList());
      });
  }

  public Mono<TransactionDto> execTransaction(WithdrawDto withdrawDto, Card card, Integer order) {
      if (Objects.isNull(card.getAccounts())
        || card.getAccounts().isEmpty()) {
         throw new CardException("The card has no linked accounts");
      }

      if (order > card.getAccounts().size()) {
         throw new CardException("You do not have enough balance in your accounts");
      }

      String number = card.getAccounts()
            .stream()
            .filter(bankAccount -> bankAccount.getOrder().equals(order))
            .map(BankAccount::getAccountNumber)
            .findFirst()
            .orElse(null);

      WithdrawTxnDto withdrawTxnDto = new WithdrawTxnDto();
      withdrawTxnDto.setAccountNumber(number);
      withdrawTxnDto.setAmount(withdrawDto.getAmount());
      withdrawTxnDto.setOperation(withdrawDto.getDetail());
      withdrawDto.setAccountNumber(number);

      return accountService.withdraw(withdrawTxnDto)
            .onErrorResume(throwable -> {
               if (throwable instanceof InsufficientBalanceException) {
                  return execTransaction(withdrawDto, card, order + 1);
               }
               return Mono.error(new CardException(throwable.getMessage()));
            });
   }

   public Mono<CardMovementDto> saveCardMovement(TransactionDto transactionDto,
                                                 WithdrawDto withdrawDto,
                                                 String cardId) {
      return Mono.just(withdrawDto)
            .map(mapperClass::toCardMovement)
            .doOnNext(cardMovement -> {
               cardMovement.setCardId(cardId);
               cardMovement.setOperationDate(transactionDto.getDate());
               cardMovement.setTransactionId(transactionDto.getId());
            })
            .flatMap(cardMovementRepository::insert)
            .map(mapperClass::toCardMovementDto);
   }
}
