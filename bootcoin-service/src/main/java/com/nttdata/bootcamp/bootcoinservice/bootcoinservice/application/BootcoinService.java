package com.nttdata.bootcamp.bootcoinservice.bootcoinservice.application;

import com.nttdata.bootcamp.bootcoinservice.bootcoinservice.model.dto.BootcoinDto;
import com.nttdata.bootcamp.bootcoinservice.bootcoinservice.model.dto.MovementsDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface BootcoinService {

  Mono<BootcoinDto> create(BootcoinDto walletDto);

  Mono<BootcoinDto> findByPhone(String phone);


  Mono<BootcoinDto> addMovements(MovementsDto movementsDto);

  Flux<BootcoinDto> findAll();
}
