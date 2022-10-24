package com.nttdata.bootcamp.accountservice.application;

import com.nttdata.bootcamp.accountservice.application.mappers.MapperFixedTermDeposit;
import com.nttdata.bootcamp.accountservice.application.mappers.MapperTransaction;
import com.nttdata.bootcamp.accountservice.application.utils.DateUtil;
import com.nttdata.bootcamp.accountservice.feignclient.ProductClient;
import com.nttdata.bootcamp.accountservice.feignclient.CustomerClient;
import com.nttdata.bootcamp.accountservice.infrastructure.FixedTermDepositAccountRepository;
import com.nttdata.bootcamp.accountservice.infrastructure.TransactionRepository;
import com.nttdata.bootcamp.accountservice.model.*;
import com.nttdata.bootcamp.accountservice.model.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class FixedTermDepositAccountServiceImpl implements AccountService<FixedTermDepositAccountDto> {
    @Autowired
    CustomerClient customerClient;
    @Autowired
    ProductClient productClient;
    @Autowired
    FixedTermDepositAccountRepository fixedTermDepositAccountRepository;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    MapperFixedTermDeposit mapperFixedTermDeposit;
    @Autowired
    MapperTransaction mapperTransaction;
    @Override
    public Mono<FixedTermDepositAccountDto> create(FixedTermDepositAccountDto accountDto) {
        accountDto.setState(StateAccount.ACTIVE);
        accountDto.setCreatedAt(new Date());
        return customerClient.getClient(accountDto.getHolderId()).flatMap(client -> {
            if(client.getTypeCustomer().equals(TypeCustomer.COMPANY)) {
                return Mono.error(new IllegalArgumentException("Customer must be personal type"));
            }
            return fixedTermDepositAccountRepository.findByHolderId(accountDto.getHolderId()).flatMap(fixedTermDepositAccount -> {
                if (fixedTermDepositAccount.getId() != null) {
                    return Mono.error(new IllegalArgumentException("The customer can only have one fixed term deposit account"));
                }
                return this.save(accountDto);
            }).switchIfEmpty(Mono.defer(() -> this.save(accountDto)));
        }).switchIfEmpty(Mono.error(new IllegalArgumentException("Customer not found")));
    }

    public Mono<FixedTermDepositAccountDto> save(FixedTermDepositAccountDto accountDto) {
        return productClient.getProductAccount(accountDto.getProductId()).flatMap(productAccountDto -> {
            if (BigDecimal.valueOf(productAccountDto.getMinFixedAmount()).compareTo(accountDto.getBalance()) == 1) {
                return Mono.error(new IllegalArgumentException("Insufficient minimum amount to open an account"));
            }
            Mono<FixedTermDepositAccount> fixedTermDepositAccountMono = mapperFixedTermDeposit.toProductAccount(accountDto)
                    .flatMap(fixedTermDepositAccountRepository::insert);
            return fixedTermDepositAccountMono
                    .flatMap(mapperFixedTermDeposit::toDto);
        });
    };

    @Override
    public Mono<FixedTermDepositAccountDto> findByHolderId(String holderId) {
        return fixedTermDepositAccountRepository.findByHolderId(holderId)
                .flatMap(mapperFixedTermDeposit::toDto);
    }

    @Override
    public Mono<FixedTermDepositAccountDto> findByNumber(String number) {
        return fixedTermDepositAccountRepository.findByNumber(number)
                .flatMap(mapperFixedTermDeposit::toDto);
    }

    @Override
    public Flux<TransactionDto> listTransactions(String accountId) {
        return transactionRepository.findByAccountId(accountId)
                .flatMap(mapperTransaction::toDto);
    }

    @Override
    public Mono<Void> delete(String accountId) {
        return fixedTermDepositAccountRepository.findByNumber(accountId)
                .flatMap(fixedTermDepositAccountRepository::delete);
    }

    @Override
    public Mono<String> deposit(OperationDto depositDto) {
        return fixedTermDepositAccountRepository.findByNumber(depositDto.getOrigAccountNumber()).flatMap(fixedTermDepositAccount -> {
            if(fixedTermDepositAccount.getDayOfOperation() != DateUtil.getCurrentDay()) {
                return Mono.error(new IllegalArgumentException("Date not allowed to perform operations"));
            }
            return productClient.getProductAccount(fixedTermDepositAccount.getProductId()).flatMap(productAccountDto-> {
                return transactionRepository.findByDateOfTransactionBetweenAndAccountId(DateUtil.getStartOfMonth(), DateUtil.getEndOfMonth(), fixedTermDepositAccount.getId()).collectList().flatMap(count -> {
                    if(productAccountDto.getMaxMovements() <= count.size()) {
                        return Mono.error(new IllegalArgumentException("Exceeded the maximum movement limit"));
                    }
                    fixedTermDepositAccount.setUpdatedAt(new Date());
                    fixedTermDepositAccount.setBalance(fixedTermDepositAccount.getBalance().add(depositDto.getAmount()));
                    depositDto.setOriginId(fixedTermDepositAccount.getId());
                    return fixedTermDepositAccountRepository.save(fixedTermDepositAccount)
                            .flatMap(ft -> this.saveTransaction(depositDto, TypeTransaction.DEPOSIT, TypeAffectation.INCREASE))
                            .then(Mono.just("Deposit completed successfully"));
                });
            }).switchIfEmpty(Mono.error(new IllegalArgumentException("Product does not exist")));
        }).switchIfEmpty(Mono.error(new IllegalArgumentException("Account number does not exist")));
    }

    @Override
    public Mono<String> withdraw(OperationDto withdrawDto) {
        return fixedTermDepositAccountRepository.findByNumber(withdrawDto.getOrigAccountNumber()).flatMap(fixedTermDepositAccount -> {
            if(fixedTermDepositAccount.getDayOfOperation() != DateUtil.getCurrentDay()) {
                return Mono.error(new IllegalArgumentException("Date not allowed to perform operations"));
            }
            if (withdrawDto.getAmount().compareTo(fixedTermDepositAccount.getBalance()) == 1) {
                return Mono.error(new IllegalArgumentException("There is not enough balance to execute the operation"));
            }
            return productClient.getProductAccount(fixedTermDepositAccount.getProductId()).flatMap(productAccountDto -> {
                return transactionRepository.findByDateOfTransactionBetweenAndAccountId(DateUtil.getStartOfMonth(), DateUtil.getEndOfMonth(), fixedTermDepositAccount.getId()).collectList().flatMap(count -> {
                    if(productAccountDto.getMaxMovements() <= count.size()) {
                        return Mono.error(new IllegalArgumentException("Exceeded the maximum movement limit"));
                    }
                    fixedTermDepositAccount.setUpdatedAt(new Date());
                    fixedTermDepositAccount.setBalance(fixedTermDepositAccount.getBalance().subtract(withdrawDto.getAmount()));
                    withdrawDto.setOriginId(fixedTermDepositAccount.getId());
                    return fixedTermDepositAccountRepository.save(fixedTermDepositAccount)
                            .flatMap(ft-> this.saveTransaction(withdrawDto, TypeTransaction.WITHDRAW, TypeAffectation.DECREMENT))
                            .then(Mono.just("Withdrawal completed successfully"));
                });
            }).switchIfEmpty(Mono.error(new IllegalArgumentException("Product does not exist")));
        }).switchIfEmpty(Mono.error(new IllegalArgumentException("Account number does not exist")));
    }

    @Override
    public Mono<String> wireTransfer(OperationDto withdrawDto) {
        return null;
    }

    public Mono<Transaction> saveTransaction(OperationDto withdrawDto, TypeTransaction typeTxn, TypeAffectation typeAffectation){
        Transaction transaction = new Transaction();
        transaction.setOrigin(withdrawDto.getOriginId());
        transaction.setDateOfTransaction(new Date());
        transaction.setAmount(withdrawDto.getAmount());
        transaction.setType(typeTxn);
        transaction.setOperation(withdrawDto.getOperation());
        transaction.setAffectation(typeAffectation);
        return transactionRepository.insert(transaction);
    }
}
