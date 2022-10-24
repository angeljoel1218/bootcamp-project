package com.nttdata.bootcamp.accountservice.application;

import com.nttdata.bootcamp.accountservice.feignclient.CreditClient;
import com.nttdata.bootcamp.accountservice.feignclient.CustomerClient;
import com.nttdata.bootcamp.accountservice.feignclient.ProductClient;
import com.nttdata.bootcamp.accountservice.infrastructure.CurrentAccountRepository;
import com.nttdata.bootcamp.accountservice.infrastructure.FixedTermAccountRepository;
import com.nttdata.bootcamp.accountservice.infrastructure.SavingsAccountRepository;
import com.nttdata.bootcamp.accountservice.infrastructure.TransactionRepository;
import com.nttdata.bootcamp.accountservice.model.*;
import com.nttdata.bootcamp.accountservice.model.dto.OperationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class OperationCurrentAccountServiceImpl implements OperationService{
    @Autowired
    CustomerClient customerClient;
    @Autowired
    ProductClient productClient;
    @Autowired
    CreditClient creditClient;
    @Autowired
    CurrentAccountRepository currentAccountRepository;
    @Autowired
    FixedTermAccountRepository fixedTermAccountRepository;
    @Autowired
    SavingsAccountRepository savingsAccountRepository;
    @Autowired
    TransactionRepository transactionRepository;

    @Override
    public Mono<String> deposit(OperationDto depositDto) {
        return currentAccountRepository.findByNumber(depositDto.getOrigAccountNumber()).flatMap(currentAccount -> {
            currentAccount.setBalance(currentAccount.getBalance().add(depositDto.getAmount()));
            currentAccount.setUpdatedAt(new Date());
            depositDto.setAccountId(currentAccount.getId());
            depositDto.setDestinationId(currentAccount.getId());
            return currentAccountRepository.save(currentAccount)
                    .flatMap(ca -> this.saveTransaction(depositDto, TypeTransaction.DEPOSIT, TypeAffectation.INCREASE))
                    .then(Mono.just("Deposit completed successfully"));
        }).switchIfEmpty(Mono.error(new IllegalArgumentException("Account number does not exist")));
    }

    @Override
    public Mono<String> withdraw(OperationDto withdrawDto) {
        return currentAccountRepository.findByNumber(withdrawDto.getOrigAccountNumber())
                .flatMap(currentAccount -> {
                    return customerClient.getClient(currentAccount.getHolderId()).flatMap(customerDto -> {
                        if(customerDto.getTypeProfile().equals(TypeProfile.VIP)) {
                            return productClient.getProductAccount(currentAccount.getProductId()).flatMap(productDto -> {
                                BigDecimal minFixedAmount = currentAccount.getBalance().subtract(withdrawDto.getAmount());
                                if (minFixedAmount.compareTo(BigDecimal.valueOf(productDto.getMinFixedAmount())) == -1 ) {
                                    return Mono.error(new IllegalArgumentException("Error the account requires a minimum amount of balance allowed"));
                                }
                                return this.execWithdraw(currentAccount, withdrawDto);
                            }).switchIfEmpty(Mono.error(new IllegalArgumentException("Product not found")));
                        }
                        if (withdrawDto.getAmount().compareTo(currentAccount.getBalance()) == 1 ) {
                            return Mono.error(new IllegalArgumentException("There is not enough balance to execute the operation"));
                        }
                        return this.execWithdraw(currentAccount, withdrawDto);
                    }).switchIfEmpty(Mono.error(new IllegalArgumentException("Customer not found")));
                }).switchIfEmpty(Mono.error(new IllegalArgumentException("Account number does not exist")));
    }

    @Override
    public Mono<String> wireTransfer(OperationDto withdrawDto) {
        return null;
    }

    public Mono<String> execWithdraw(CurrentAccount currentAccount, OperationDto withdrawDto) {
        currentAccount.setBalance(currentAccount.getBalance().subtract(withdrawDto.getAmount()));
        currentAccount.setUpdatedAt(new Date());
        withdrawDto.setAccountId(currentAccount.getId());
        withdrawDto.setOriginId(currentAccount.getId());
        return currentAccountRepository.save(currentAccount)
                .flatMap(ca -> this.saveTransaction(withdrawDto, TypeTransaction.WITHDRAW, TypeAffectation.DECREMENT))
                .then(Mono.just("Withdrawal completed successfully"));
    }

    public Mono<Transaction> saveTransaction(OperationDto withdrawDto, TypeTransaction typeTxn, TypeAffectation typeAffectation){
        Transaction transaction = new Transaction();
        transaction.setAccountId(withdrawDto.getAccountId());
        transaction.setOrigin(withdrawDto.getOriginId());
        transaction.setDestination(withdrawDto.getDestinationId());
        transaction.setDateOfTransaction(new Date());
        transaction.setAmount(withdrawDto.getAmount());
        transaction.setType(typeTxn);
        transaction.setOperation(withdrawDto.getOperation());
        transaction.setAffectation(typeAffectation);
        transaction.setTypeAccount(TypeAccount.CURRENT_ACCOUNT);
        return transactionRepository.insert(transaction);
    }
}
