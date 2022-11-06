package com.nttdata.bootcamp.adbootcoinservice.infrastructure;

import com.nttdata.bootcamp.adbootcoinservice.domain.model.PayOrder;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface PayOrderRepository extends ReactiveMongoRepository<PayOrder, String> {
}
