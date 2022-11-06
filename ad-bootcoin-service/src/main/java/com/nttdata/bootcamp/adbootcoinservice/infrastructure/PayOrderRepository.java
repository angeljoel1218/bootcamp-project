package com.nttdata.bootcamp.adbootcoinservice.infrastructure;

import com.nttdata.bootcamp.adbootcoinservice.domain.model.PayOrder;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 *
 * @since 2022
 */
public interface PayOrderRepository extends ReactiveMongoRepository<PayOrder, String> {
}
