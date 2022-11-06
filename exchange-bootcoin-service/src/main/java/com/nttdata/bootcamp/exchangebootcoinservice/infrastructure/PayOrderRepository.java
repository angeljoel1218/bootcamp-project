package com.nttdata.bootcamp.exchangebootcoinservice.infrastructure;

import com.nttdata.bootcamp.exchangebootcoinservice.domain.model.PayOrder;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface PayOrderRepository extends ReactiveMongoRepository<PayOrder, String> {
}
