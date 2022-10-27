package com.nttdata.bootcamp.accountservice.application;

import com.nttdata.bootcamp.accountservice.application.exceptions.OperationAccountException;
import com.nttdata.bootcamp.accountservice.infrastructure.*;
import com.nttdata.bootcamp.accountservice.model.Transaction;
import com.nttdata.bootcamp.accountservice.model.constant.TypeAccount;
import com.nttdata.bootcamp.accountservice.model.constant.TypeAffectation;
import com.nttdata.bootcamp.accountservice.model.constant.TypeTransaction;
import com.nttdata.bootcamp.accountservice.model.dto.OperationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Date;

@Service
public class TransferBalanceService {
    @Autowired
    SavingsAccountRepository savingsAccountRepository;
    @Autowired
    FixedTermAccountRepository fixedTermAccountRepository;
    @Autowired
    CurrentAccountRepository currentAccountRepository;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    AccountRepository accountRepository;
    public Mono<OperationDto> saveTransferOut(OperationDto operationDto) {
        return accountRepository.findByNumber(operationDto.getDestAccountNumber()).flatMap(account -> {
            if (account.getTypeAccount().equals(TypeAccount.SAVINGS_ACCOUNT)) {
                return savingsAccountRepository.findByNumberAndTypeAccount(operationDto.getDestAccountNumber(), TypeAccount.SAVINGS_ACCOUNT).flatMap(savingsAccount -> {
                            operationDto.setAccountId(savingsAccount.getId());
                            savingsAccount.setUpdatedAt(new Date());
                            savingsAccount.setBalance(savingsAccount.getBalance().add(operationDto.getAmount()));
                            return savingsAccountRepository.save(savingsAccount);
                        }).flatMap(rs-> this.saveTransaction(operationDto, TypeAccount.SAVINGS_ACCOUNT))
                        .then(Mono.just(operationDto));
            }
            if (account.getTypeAccount().equals(TypeAccount.CURRENT_ACCOUNT)) {
                return currentAccountRepository.findByNumberAndTypeAccount(operationDto.getDestAccountNumber(),TypeAccount.CURRENT_ACCOUNT).flatMap(currentAccount -> {
                            operationDto.setAccountId(currentAccount.getId());
                            currentAccount.setUpdatedAt(new Date());
                            currentAccount.setBalance(currentAccount.getBalance().add(operationDto.getAmount()));
                            return currentAccountRepository.save(currentAccount);
                        }).flatMap(rs-> this.saveTransaction(operationDto, TypeAccount.CURRENT_ACCOUNT))
                        .then(Mono.just(operationDto));
            }
            if (account.getTypeAccount().equals(TypeAccount.FIXED_TERM_ACCOUNT)) {
                return fixedTermAccountRepository.findByNumberAndTypeAccount(operationDto.getDestAccountNumber(),TypeAccount.FIXED_TERM_ACCOUNT).flatMap(fixedTermAccount -> {
                            operationDto.setAccountId(fixedTermAccount.getId());
                            fixedTermAccount.setUpdatedAt(new Date());
                            fixedTermAccount.setBalance(fixedTermAccount.getBalance().add(operationDto.getAmount()));
                            return fixedTermAccountRepository.save(fixedTermAccount);
                        }).flatMap(rs-> this.saveTransaction(operationDto, TypeAccount.FIXED_TERM_ACCOUNT))
                        .then(Mono.just(operationDto));
            }
            return Mono.error(new OperationAccountException("Account destination does not exist"));
        }).switchIfEmpty(Mono.error(new OperationAccountException("Account destination does not exist")));
    }
    public Mono<Transaction> saveTransaction(OperationDto withdrawDto, TypeAccount typeAccount){
        Transaction transaction = new Transaction();
        transaction.setAccountId(withdrawDto.getAccountId());
        transaction.setOrigin(withdrawDto.getOrigAccountNumber());
        transaction.setDestination(withdrawDto.getDestAccountNumber());
        transaction.setDateOfTransaction(new Date());
        transaction.setAmount(withdrawDto.getAmount());
        transaction.setType(TypeTransaction.TRANSFER);
        transaction.setOperation(withdrawDto.getOperation());
        transaction.setAffectation(TypeAffectation.INCREASE);
        transaction.setTypeAccount(typeAccount);
        return transactionRepository.insert(transaction);
    }
}
