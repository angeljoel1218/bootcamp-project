package com.nttdata.bootcamp.adbootcoinservice.infrastructure;

import com.nttdata.bootcamp.adbootcoinservice.domain.constant.StateAdvert;
import com.nttdata.bootcamp.adbootcoinservice.domain.model.Advert;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

/**
 *
 * @since 2022
 */

public interface AdvertRepository extends ReactiveMongoRepository<Advert, String> {
    Flux<Advert> findByState(StateAdvert stateAdvert);
}
