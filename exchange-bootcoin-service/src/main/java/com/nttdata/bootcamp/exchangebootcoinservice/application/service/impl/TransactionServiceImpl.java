package com.nttdata.bootcamp.exchangebootcoinservice.application.service.impl;

import com.nttdata.bootcamp.exchangebootcoinservice.application.mapper.MapperPayOrder;
import com.nttdata.bootcamp.exchangebootcoinservice.application.mapper.MapperTransaction;
import com.nttdata.bootcamp.exchangebootcoinservice.application.service.TransactionService;
import com.nttdata.bootcamp.exchangebootcoinservice.domain.constant.MethodPayment;
import com.nttdata.bootcamp.exchangebootcoinservice.domain.constant.StateTransaction;
import com.nttdata.bootcamp.exchangebootcoinservice.domain.dto.*;
import com.nttdata.bootcamp.exchangebootcoinservice.domain.model.PayOrder;
import com.nttdata.bootcamp.exchangebootcoinservice.domain.model.Transaction;
import com.nttdata.bootcamp.exchangebootcoinservice.infrastructure.ConfigPaymentMethodRepository;
import com.nttdata.bootcamp.exchangebootcoinservice.infrastructure.PayOrderRepository;
import com.nttdata.bootcamp.exchangebootcoinservice.infrastructure.TransactionRepository;
import com.nttdata.bootcamp.exchangebootcoinservice.infrastructure.events.ProducerBootcoinBalance;
import com.nttdata.bootcamp.exchangebootcoinservice.infrastructure.events.ProducerTransactionAccount;
import com.nttdata.bootcamp.exchangebootcoinservice.infrastructure.events.ProducerTransactionWallet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.xml.transform.TransformerException;
import java.util.Date;

@Slf4j
@Service
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    PayOrderRepository payOrderRepository;

    @Autowired
    ConfigPaymentMethodRepository configPaymentMethodRepository;

    @Autowired
    ProducerTransactionAccount producerTransactionAccount;

    @Autowired
    ProducerTransactionWallet producerTransactionWallet;

    @Autowired
    ProducerBootcoinBalance producerBootcoinBalance;

    @Autowired
    MapperTransaction mapperTransaction;

    @Autowired
    MapperPayOrder mapperPayOrder;

    @Override
    public Mono<TransactionDto> acceptRequest(String orderId) {
        return payOrderRepository
                .findById(orderId)
                .switchIfEmpty(Mono.error(new TransformerException("Payment order not found")))
                .flatMap(payOrder -> {
                    Transaction transaction = new Transaction();
                    transaction.setNumber(String.valueOf(System.currentTimeMillis()));
                    transaction.setOrderId(orderId);
                    transaction.setAmountPay(payOrder.getAmountPay());
                    transaction.setAmount(payOrder.getAmount());
                    transaction.setMethodPayment(payOrder.getMethodPayment());
                    transaction.setSellerWalletId(payOrder.getSellerWalletId());
                    transaction.setBuyerWalletId(payOrder.getBuyerWalletId());
                    transaction.setState(StateTransaction.PENDING);
                    transaction.setCreatedAt(new Date());
                    return Mono.just(transaction)
                            .flatMap(transactionRepository::insert)
                            .map(t-> this.sendTransaction(t, payOrder))
                            .map(mapperTransaction::toDto);
                });
    }

    @Override
    public Mono<Void> receiveTransactionConfirmation(TransactionBootcoinDto transactionBootcoinDto) {
        return transactionRepository.findById(transactionBootcoinDto.getTransactionId())
                .flatMap(transaction -> {
                    transaction.setState(StateTransaction.DONE);
                    return transactionRepository.save(transaction);
                })
                .doOnNext(transaction -> {
                    RequestBootcoin requestBootcoin = new RequestBootcoin();
                    requestBootcoin.setAmount(transaction.getAmount());
                    requestBootcoin.setSourceWalletId(transaction.getSellerWalletId());
                    requestBootcoin.setTargetWalletId(transaction.getBuyerWalletId());
                    producerBootcoinBalance.sendMessage(requestBootcoin);
                }).then();
    }

    @Override
    public Mono<Void> purchaseRequest(PayOrderDto payOrderDto) {
        return configPaymentMethodRepository.findByWalletId(payOrderDto.getSellerWalletId())
                .flatMap(configPaymentMethod -> configPaymentMethodRepository.findByWalletId(payOrderDto.getBuyerWalletId())
                        .flatMap(configPaymentMethod1 -> {
                            PayOrder payOrder = mapperPayOrder.toPayOrder(payOrderDto);
                            if(payOrderDto.getMethodPayment().equals(MethodPayment.TRANSFER.getAction())) {
                                payOrder.setConfigPayment(new ConfigPayment(configPaymentMethod1.getNumberAccount(), configPaymentMethod.getNumberAccount()));
                            } else if (payOrderDto.getMethodPayment().equals(MethodPayment.YANKI.getAction())) {
                                payOrder.setConfigPayment(new ConfigPayment(configPaymentMethod1.getNumberCellPhone(), configPaymentMethod.getNumberCellPhone()));
                            }
                            return Mono.just(payOrder)
                                    .map(payOrderRepository::insert)
                                    .then();
                        }));
    }

    private Transaction sendTransaction(Transaction transaction, PayOrder payOrder) {
        log.debug("sendBalance executed {}", transaction);
        if (transaction != null) {
            if(transaction.getMethodPayment().equals(MethodPayment.TRANSFER.getAction())) {
                producerTransactionAccount.sendMessage(TransactionBootcoinDto
                        .builder()
                        .transactionId(transaction.getId())
                        .amount(transaction.getAmountPay())
                        .sourceNumber(payOrder.getConfigPayment().getSourceNumber())
                        .targetNumber(payOrder.getConfigPayment().getTargetNumber())
                        .build());
            } else if (transaction.getMethodPayment().equals(MethodPayment.YANKI.getAction())) {
                producerTransactionWallet.sendMessage(TransactionBootcoinDto
                        .builder()
                        .transactionId(transaction.getId())
                        .amount(transaction.getAmountPay())
                        .sourceNumber(payOrder.getConfigPayment().getSourceNumber())
                        .targetNumber(payOrder.getConfigPayment().getTargetNumber())
                        .build());
            }
        }
        return transaction;
    }
}
