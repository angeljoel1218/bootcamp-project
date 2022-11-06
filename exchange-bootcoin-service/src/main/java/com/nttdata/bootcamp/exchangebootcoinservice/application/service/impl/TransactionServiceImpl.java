package com.nttdata.bootcamp.exchangebootcoinservice.application.service.impl;

import com.nttdata.bootcamp.exchangebootcoinservice.application.mapper.MapperTransaction;
import com.nttdata.bootcamp.exchangebootcoinservice.application.service.TransactionService;
import com.nttdata.bootcamp.exchangebootcoinservice.domain.constant.StateTransaction;
import com.nttdata.bootcamp.exchangebootcoinservice.domain.dto.TransactionDto;
import com.nttdata.bootcamp.exchangebootcoinservice.domain.model.Transaction;
import com.nttdata.bootcamp.exchangebootcoinservice.infrastructure.ConfigPaymentMethodRepository;
import com.nttdata.bootcamp.exchangebootcoinservice.infrastructure.PayOrderRepository;
import com.nttdata.bootcamp.exchangebootcoinservice.infrastructure.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.xml.transform.TransformerException;
import java.util.Date;

@Service
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    PayOrderRepository payOrderRepository;
    @Autowired
    ConfigPaymentMethodRepository configPaymentMethodRepository;
    @Autowired
    MapperTransaction mapperTransaction;

    @Override
    public Mono<TransactionDto> acceptRequest(String orderId) {
        return payOrderRepository
                .findById(orderId)
                .switchIfEmpty(Mono.error(new TransformerException("Payment order not found")))
                .flatMap(payOrder -> {
                    Transaction transaction = new Transaction();
                    transaction.setNumber(String.valueOf(System.currentTimeMillis()));
                    transaction.setOrderId(orderId);
                    transaction.setSellerWalletId(payOrder.getSellerWalletId());
                    transaction.setBuyerWalletId(payOrder.getBuyerWalletId());
                    transaction.setState(StateTransaction.PENDING);
                    transaction.setCreatedAt(new Date());
                    return Mono.just(transaction)
                            .flatMap(transactionRepository::insert)
                            .map(mapperTransaction::toDto);
                });
    }
}
