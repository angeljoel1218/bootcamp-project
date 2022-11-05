package com.nttdata.bootcamp.bootcoinservice.bootcoinservice.infrastructure;

import com.nttdata.bootcamp.bootcoinservice.bootcoinservice.model.BootcoinMovements;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface BootcointMovementsRepository extends ReactiveMongoRepository<BootcoinMovements, String> {

}
