package com.nttdata.bootcamp.accountservice.application;

import com.nttdata.bootcamp.accountservice.application.exceptions.OperationAccountException;
import com.nttdata.bootcamp.accountservice.feignclient.CustomerClient;
import com.nttdata.bootcamp.accountservice.feignclient.ProductClient;
import com.nttdata.bootcamp.accountservice.infrastructure.CurrentAccountRepository;
import com.nttdata.bootcamp.accountservice.infrastructure.TransactionRepository;
import com.nttdata.bootcamp.accountservice.model.*;
import com.nttdata.bootcamp.accountservice.model.constant.TypeAccount;
import com.nttdata.bootcamp.accountservice.model.constant.TypeAffectation;
import com.nttdata.bootcamp.accountservice.model.constant.TypeProfile;
import com.nttdata.bootcamp.accountservice.model.constant.TypeTransaction;
import com.nttdata.bootcamp.accountservice.model.dto.CurrentAccountDto;
import com.nttdata.bootcamp.accountservice.model.dto.OperationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class CurrentAccountOperationServiceImpl implements OperationService<CurrentAccountDto>{
    @Autowired
    CustomerClient customerClient;
    @Autowired
    ProductClient productClient;
    @Autowired
    CurrentAccountRepository currentAccountRepository;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    TransferBalanceService transferBalanceService;

    @Override
    public Mono<String> deposit(OperationDto depositDto) {
        return currentAccountRepository.findByNumberAndTypeAccount(depositDto.getDestAccountNumber(), TypeAccount.CURRENT_ACCOUNT).flatMap(currentAccount -> {
            currentAccount.setBalance(currentAccount.getBalance().add(depositDto.getAmount()));
            currentAccount.setUpdatedAt(new Date());
            depositDto.setAccountId(currentAccount.getId());
            return currentAccountRepository.save(currentAccount)
                    .flatMap(ca -> this.saveTransaction(depositDto, TypeTransaction.DEPOSIT, TypeAffectation.INCREASE, TypeAccount.CURRENT_ACCOUNT))
                    .then(Mono.just("Deposit completed successfully"));
        }).switchIfEmpty(Mono.error(new OperationAccountException("Account number does not exist")));
    }
    @Override
    public Mono<String> withdraw(OperationDto withdrawDto) {
        return currentAccountRepository.findByNumberAndTypeAccount(withdrawDto.getOrigAccountNumber(), TypeAccount.CURRENT_ACCOUNT)
                .flatMap(currentAccount -> {
                    return customerClient.getClient(currentAccount.getHolderId()).flatMap(customerDto -> {
                        if(customerDto.getTypeProfile() != null && customerDto.getTypeProfile().equals(TypeProfile.VIP)) {
                            return productClient.getProductAccount(currentAccount.getProductId()).flatMap(productDto -> {
                                BigDecimal minFixedAmount = currentAccount.getBalance().subtract(withdrawDto.getAmount());
                                if (minFixedAmount.compareTo(BigDecimal.valueOf(productDto.getMinFixedAmount())) == -1 ) {
                                    return Mono.error(new OperationAccountException("Error the account requires a minimum amount of balance allowed"));
                                }
                                return this.execWithdraw(currentAccount, withdrawDto);
                            }).switchIfEmpty(Mono.error(new OperationAccountException("Product not found")));
                        }
                        if (withdrawDto.getAmount().compareTo(currentAccount.getBalance()) == 1 ) {
                            return Mono.error(new OperationAccountException("There is not enough balance to execute the operation"));
                        }
                        return this.execWithdraw(currentAccount, withdrawDto);
                    }).switchIfEmpty(Mono.error(new OperationAccountException("Customer not found")));
                }).switchIfEmpty(Mono.error(new OperationAccountException("Account number does not exist")));
    }
    @Override
    public Mono<String> wireTransfer(OperationDto operationDto) {
        return currentAccountRepository.findByNumberAndTypeAccount(operationDto.getOrigAccountNumber(), TypeAccount.CURRENT_ACCOUNT)
                .flatMap(currentAccount -> {
                    return customerClient.getClient(currentAccount.getHolderId()).flatMap(customerDto -> {
                        if(customerDto.getTypeProfile() != null && customerDto.getTypeProfile().equals(TypeProfile.VIP)) {
                            return productClient.getProductAccount(currentAccount.getProductId()).flatMap(productDto -> {
                                BigDecimal minFixedAmount = currentAccount.getBalance().subtract(operationDto.getAmount());
                                if (minFixedAmount.compareTo(BigDecimal.valueOf(productDto.getMinFixedAmount())) == -1 ) {
                                    return Mono.error(new OperationAccountException("Error the account requires a minimum amount of balance allowed"));
                                }
                                return this.transferBalanceService.saveTransferOut(operationDto)
                                        .flatMap(operationDto1 -> {
                                            currentAccount.setBalance(currentAccount.getBalance().subtract(operationDto1.getAmount()));
                                            currentAccount.setUpdatedAt(new Date());
                                            operationDto.setAccountId(currentAccount.getId());
                                            return currentAccountRepository.save(currentAccount)
                                                    .flatMap(rs -> this.saveTransaction(operationDto, TypeTransaction.TRANSFER, TypeAffectation.DECREMENT, TypeAccount.SAVINGS_ACCOUNT))
                                                    .then(Mono.just("Transfer completed successfully"));
                                        });
                            }).switchIfEmpty(Mono.error(new OperationAccountException("Product not found")));
                        }
                        if (operationDto.getAmount().compareTo(currentAccount.getBalance()) == 1 ) {
                            return Mono.error(new OperationAccountException("There is not enough balance to execute the operation"));
                        }
                        return this.transferBalanceService.saveTransferOut(operationDto)
                                .flatMap(operationDto1 -> {
                                    currentAccount.setBalance(currentAccount.getBalance().subtract(operationDto1.getAmount()));
                                    currentAccount.setUpdatedAt(new Date());
                                    operationDto.setAccountId(currentAccount.getId());
                                    return currentAccountRepository.save(currentAccount)
                                            .flatMap(rs -> this.saveTransaction(operationDto, TypeTransaction.TRANSFER, TypeAffectation.DECREMENT, TypeAccount.SAVINGS_ACCOUNT))
                                            .then(Mono.just("Transfer completed successfully"));
                                });
                    }).switchIfEmpty(Mono.error(new OperationAccountException("Client does not exist")));
                }).switchIfEmpty(Mono.error(new OperationAccountException("Account number does not exist")));
    }
    public Mono<String> execWithdraw(CurrentAccount currentAccount, OperationDto withdrawDto) {
        currentAccount.setBalance(currentAccount.getBalance().subtract(withdrawDto.getAmount()));
        currentAccount.setUpdatedAt(new Date());
        withdrawDto.setAccountId(currentAccount.getId());
        return currentAccountRepository.save(currentAccount)
                .flatMap(ca -> this.saveTransaction(withdrawDto, TypeTransaction.WITHDRAW, TypeAffectation.DECREMENT, TypeAccount.CURRENT_ACCOUNT))
                .then(Mono.just("Withdrawal completed successfully"));
    }
    public Mono<Transaction> saveTransaction(OperationDto operationDto, TypeTransaction typeTxn, TypeAffectation typeAffectation, TypeAccount typeAccount){
        Transaction transaction = new Transaction();
        transaction.setAccountId(operationDto.getAccountId());
        transaction.setOrigin(operationDto.getOrigAccountNumber());
        transaction.setDestination(operationDto.getDestAccountNumber());
        transaction.setDateOfTransaction(new Date());
        transaction.setAmount(operationDto.getAmount());
        transaction.setType(typeTxn);
        transaction.setOperation(operationDto.getOperation());
        transaction.setAffectation(typeAffectation);
        transaction.setTypeAccount(typeAccount);
        return transactionRepository.insert(transaction);
    }
}
