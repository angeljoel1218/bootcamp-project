package com.nttdata.bootcamp.wallettransactionservice.application.servivce.impl;

import com.nttdata.bootcamp.wallettransactionservice.application.exception.InsufficientBalanceException;
import com.nttdata.bootcamp.wallettransactionservice.application.exception.WalletException;
import com.nttdata.bootcamp.wallettransactionservice.application.mapper.MapperGeneric;
import com.nttdata.bootcamp.wallettransactionservice.application.servivce.TransactionService;
import com.nttdata.bootcamp.wallettransactionservice.infrastructure.LastTransactionRepository;
import com.nttdata.bootcamp.wallettransactionservice.infrastructure.TransactionRepository;
import com.nttdata.bootcamp.wallettransactionservice.infrastructure.producer.BalanceProducer;
import com.nttdata.bootcamp.wallettransactionservice.infrastructure.webclient.WalletClient;
import com.nttdata.bootcamp.wallettransactionservice.model.LastTransaction;
import com.nttdata.bootcamp.wallettransactionservice.model.Transaction;
import com.nttdata.bootcamp.wallettransactionservice.model.dto.TransactionDto;
import com.nttdata.bootcamp.wallettransactionservice.model.dto.TransactionRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Date;

@Slf4j
@Service
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    LastTransactionRepository lastTransactionRepository;

    @Autowired
    WalletClient walletClient;

    @Autowired
    MapperGeneric mapperGeneric;

    @Autowired
    private BalanceProducer balanceProducer;

    @Override
    public Mono<TransactionDto> transfer(TransactionRequestDto transactionRequestDto) {
        return walletClient.getWallet(transactionRequestDto.getSourceNumberCell())
                .switchIfEmpty(Mono.error(new WalletException("The source wallet not found")))
                .doOnNext(walletDto -> {
                    if(walletDto.getBalance().compareTo(transactionRequestDto.getAmount()) == -1){
                        throw new InsufficientBalanceException("You do not have enough balance in your wallet");
                    }
                })
                .flatMap(srcWalletDto -> {
                    return walletClient.getWallet(transactionRequestDto.getTargetNumberCell())
                            .switchIfEmpty(Mono.error(new WalletException("The target wallet not found")))
                            .map(targetWalletDto -> this.sendBalance(transactionRequestDto))
                            .flatMap(t -> {
                                Transaction transaction = new Transaction();
                                transaction.setAmount(transactionRequestDto.getAmount());
                                transaction.setSourceWallet(transactionRequestDto.getSourceNumberCell());
                                transaction.setTargetWallet(transactionRequestDto.getTargetNumberCell());
                                transaction.setDate(new Date());
                                return Mono.just(transaction)
                                        .flatMap(transactionRepository::insert)
                                        .doOnNext(transaction1 -> {
                                            LastTransaction lastTransaction = new LastTransaction();
                                            lastTransaction.setId(srcWalletDto.getId());
                                            lastTransaction.setAmount(transactionRequestDto.getAmount());
                                            lastTransaction.setSourceWallet(transactionRequestDto.getSourceNumberCell());
                                            lastTransaction.setTargetWallet(transactionRequestDto.getTargetNumberCell());
                                            lastTransactionRepository.save(lastTransaction).subscribe();
                                        })
                                        .map(mapperGeneric::toTransactionDto);
                            });
                });
    }

    @Override
    public Mono<LastTransaction> lastTransaction(String walletId) {
        return lastTransactionRepository.findById(walletId)
                .switchIfEmpty(Mono.error(new WalletException("Transaction not found")));
    }

    private TransactionRequestDto sendBalance(TransactionRequestDto transactionRequestDto) {
        log.debug("sendBalance executed {}", transactionRequestDto);
        transactionRequestDto.setId("83477834687");
        if (transactionRequestDto != null) {
            balanceProducer.sendMessage(TransactionRequestDto.builder().id(transactionRequestDto.getId()).sourceNumberCell(transactionRequestDto.getSourceNumberCell()).targetNumberCell(transactionRequestDto.getTargetNumberCell()).amount(transactionRequestDto.getAmount()).build());
        }
        return transactionRequestDto;
    }
}
