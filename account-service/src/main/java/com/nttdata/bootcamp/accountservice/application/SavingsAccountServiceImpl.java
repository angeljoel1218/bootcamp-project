package com.nttdata.bootcamp.accountservice.application;

import com.nttdata.bootcamp.accountservice.application.mappers.MapperSavingsAccount;
import com.nttdata.bootcamp.accountservice.application.mappers.MapperTransaction;
import com.nttdata.bootcamp.accountservice.application.utils.DateUtil;
import com.nttdata.bootcamp.accountservice.feignclient.CreditClient;
import com.nttdata.bootcamp.accountservice.feignclient.ProductClient;
import com.nttdata.bootcamp.accountservice.feignclient.CustomerClient;
import com.nttdata.bootcamp.accountservice.infrastructure.SavingsAccountRepository;
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
public class SavingsAccountServiceImpl implements AccountService<SavingsAccountDto> {
    @Autowired
    MapperSavingsAccount mapperSavingsAccount;

    @Autowired
    MapperTransaction mapperTransaction;

    @Autowired
    ProductClient productClient;

    @Autowired
    CustomerClient customerClient;

    @Autowired
    CreditClient creditClient;

    @Autowired
    SavingsAccountRepository savingsAccountRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Override
    public Mono<SavingsAccountDto> create(SavingsAccountDto accountDto) {
        accountDto.setState(StateAccount.ACTIVE);
        accountDto.setCreatedAt(new Date());
        return customerClient.getClient(accountDto.getHolderId()).flatMap(client -> {
            if(client.getTypeCustomer().equals(TypeCustomer.COMPANY)) {
                return Mono.error(new IllegalArgumentException("Customer must be personal type"));
            }

            if(client.getTypeProfile().equals(TypeProfile.VIP)) {
                return creditClient.getCreditCardCustomer(accountDto.getHolderId())
                        .flatMap(creditCardDto -> {
                           if(creditCardDto.getId() != null) {
                               return this.findAndSave(accountDto);
                           }
                           return Mono.error(new IllegalArgumentException("The customer must have a credit card to enable this account"));
                        }).switchIfEmpty(Mono.error(new IllegalArgumentException("The customer must have a credit card to enable this account")));
            }

            return this.findAndSave(accountDto);
        }).switchIfEmpty(Mono.error(new IllegalArgumentException("Customer not found")));
    }

    public Mono<SavingsAccountDto> findAndSave(SavingsAccountDto accountDto) {
        return savingsAccountRepository.findByHolderId(accountDto.getHolderId()).flatMap(savingsAccount -> {
            if (savingsAccount.getId() != null) {
                return Mono.error(new IllegalArgumentException("The customer can only have one savings account"));
            }
            return this.save(accountDto);
        }).switchIfEmpty(Mono.defer(() -> this.save(accountDto)));
    }

    public Mono<SavingsAccountDto> save(SavingsAccountDto accountDto) {
        return productClient.getProductAccount(accountDto.getProductId()).flatMap(productAccountDto -> {
            if (accountDto.getBalance().compareTo(BigDecimal.valueOf(productAccountDto.getMinFixedAmount())) >= 0) {
                return Mono.error(new IllegalArgumentException("Insufficient minimum amount to open an account"));
            }
            Mono<SavingsAccount> savingsAccountMono = mapperSavingsAccount.toSavingsAccount(accountDto)
                    .flatMap(savingsAccountRepository::insert);

            return savingsAccountMono.flatMap(mapperSavingsAccount::toDto);
        });
    };

    @Override
    public Mono<SavingsAccountDto> findByHolderId(String holderId) {
        return savingsAccountRepository.findByHolderId(holderId)
                .flatMap(mapperSavingsAccount::toDto);
    }
    @Override
    public Mono<SavingsAccountDto> findByNumber(String number) {
        return savingsAccountRepository.findByNumber(number)
                .flatMap(mapperSavingsAccount::toDto);
    }

    @Override
    public Flux<TransactionDto> listTransactions(String accountId) {
        return transactionRepository.findByAccountId(accountId)
                .flatMap(mapperTransaction::toDto);
    }

    @Override
    public Mono<Void> delete(String accountId) {
        return savingsAccountRepository.findById(accountId)
                .flatMap(savingsAccountRepository::delete);
    }

    @Override
    public Mono<String> deposit(OperationDto depositDto) {
        return savingsAccountRepository.findByNumber(depositDto.getOrigAccountNumber()).flatMap(savingsAccount -> {
            return productClient.getProductAccount(savingsAccount.getProductId()).flatMap(productAccountDto -> {
                return transactionRepository.findByDateOfTransactionBetweenAndAccountId(DateUtil.getStartOfMonth(), DateUtil.getEndOfMonth(), savingsAccount.getId()).collectList().flatMap(count -> {
                    if(productAccountDto.getMaxMovements() <= count.size()) {
                        return Mono.error(new IllegalArgumentException("Exceeded the maximum movement limit"));
                    }
                    savingsAccount.setBalance(savingsAccount.getBalance().add(depositDto.getAmount()));
                    savingsAccount.setUpdatedAt(new Date());
                    depositDto.setOriginId(savingsAccount.getId());
                    return savingsAccountRepository.save(savingsAccount)
                            .flatMap(sa -> this.saveTransaction(depositDto, TypeTransaction.DEPOSIT, TypeAffectation.INCREASE))
                            .then(Mono.just("Deposit completed successfully"));
                });
            }).switchIfEmpty(Mono.error(new IllegalArgumentException("Product does not exist")));
        }).switchIfEmpty(Mono.error(new IllegalArgumentException("Account number does not exist")));
    }

    @Override
    public Mono<String> withdraw(OperationDto withdrawDto) {
        return savingsAccountRepository.findByNumber(withdrawDto.getOrigAccountNumber()).flatMap(savingsAccount -> {
            if (withdrawDto.getAmount().compareTo(savingsAccount.getBalance()) == 1) {
                return Mono.error(new IllegalArgumentException("There is not enough balance to execute the operation"));
            }
            return productClient.getProductAccount(savingsAccount.getProductId()).flatMap(productAccountDto -> {
                return transactionRepository.findByDateOfTransactionBetweenAndAccountId(DateUtil.getStartOfMonth(), DateUtil.getEndOfMonth(), savingsAccount.getId()).collectList().flatMap(count-> {
                    if(productAccountDto.getMaxMovements() <= count.size()) {
                        return Mono.error(new IllegalArgumentException("Exceeded the maximum movement limit"));
                    }
                    savingsAccount.setBalance(savingsAccount.getBalance().subtract(withdrawDto.getAmount()));
                    savingsAccount.setUpdatedAt(new Date());
                    withdrawDto.setOriginId(savingsAccount.getId());
                    return savingsAccountRepository.save(savingsAccount)
                            .flatMap(sa -> this.saveTransaction(withdrawDto, TypeTransaction.WITHDRAW, TypeAffectation.DECREMENT))
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
