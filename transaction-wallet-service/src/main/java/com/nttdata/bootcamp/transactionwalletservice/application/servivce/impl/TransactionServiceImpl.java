package com.nttdata.bootcamp.transactionwalletservice.application.servivce.impl;

import com.nttdata.bootcamp.transactionwalletservice.application.exception.InsufficientBalanceException;
import com.nttdata.bootcamp.transactionwalletservice.application.exception.WalletException;
import com.nttdata.bootcamp.transactionwalletservice.application.servivce.TransactionService;
import com.nttdata.bootcamp.transactionwalletservice.infrastructure.TransactionRepository;
import com.nttdata.bootcamp.transactionwalletservice.infrastructure.webclient.WalletClient;
import com.nttdata.bootcamp.transactionwalletservice.model.dto.TransactionDto;
import com.nttdata.bootcamp.transactionwalletservice.model.dto.TransactionRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    WalletClient walletClient;

    @Override
    public Mono<TransactionDto> transfer(TransactionRequestDto transactionRequestDto) {
        return walletClient.getWallet(transactionRequestDto.getSourceNumberCell())
                .switchIfEmpty(Mono.error(new WalletException("The source wallet not found")))
                .doOnNext(walletDto -> {
                    if(true){
                        throw new InsufficientBalanceException("You do not have enough balance in your wallet");
                    }
                })
                .flatMap(srcWalletDto -> {
                    return walletClient.getWallet(transactionRequestDto.getTargetNumberCell())
                            .switchIfEmpty(Mono.error(new WalletException("The target wallet not found")))
                            .flatMap(targetWalletDto -> {
                               return walletClient.depositMoney(transactionRequestDto)
                                       .flatMap(walletTransactionDto -> {
                                           return walletClient.withdrawals(transactionRequestDto);
                                       });
                            })
                            .flatMap(transactionRepository::insert);
                });
    }
}
