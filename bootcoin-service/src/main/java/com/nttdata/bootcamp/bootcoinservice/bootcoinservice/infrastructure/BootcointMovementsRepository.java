package com.nttdata.bootcamp.bootcoinservice.bootcoinservice.infrastructure;

import com.nttdata.bootcamp.bootcoinservice.bootcoinservice.model.BootcoinMovements;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * Some javadoc.
 *
 * @since 2022
 */
public interface BootcointMovementsRepository extends
    ReactiveMongoRepository<BootcoinMovements, String> {

}
