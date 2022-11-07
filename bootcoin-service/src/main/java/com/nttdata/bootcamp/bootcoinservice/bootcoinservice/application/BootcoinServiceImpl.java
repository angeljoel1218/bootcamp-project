package com.nttdata.bootcamp.bootcoinservice.bootcoinservice.application;

import com.nttdata.bootcamp.bootcoinservice.bootcoinservice.application.exception.InsufficientBalanceException;
import com.nttdata.bootcamp.bootcoinservice.bootcoinservice.application.mappers.MapperBootcoin;
import com.nttdata.bootcamp.bootcoinservice.bootcoinservice.infrastructure.BootcoinRepository;
import com.nttdata.bootcamp.bootcoinservice.bootcoinservice.infrastructure.BootcointMovementsRepository;
import com.nttdata.bootcamp.bootcoinservice.bootcoinservice.model.BootcoinMovements;
import com.nttdata.bootcamp.bootcoinservice.bootcoinservice.model.dto.BootcoinDto;
import com.nttdata.bootcamp.bootcoinservice.bootcoinservice.model.dto.MovementsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.naming.InsufficientResourcesException;
import java.math.BigDecimal;
import java.time.LocalDate;


/**
 * Some javadoc.
 *
 * @since 2022
 */

@Service
public class BootcoinServiceImpl implements BootcoinService {

  @Autowired
  BootcoinRepository bootcoinRepository;

  @Autowired
  BootcointMovementsRepository movementsRepository;


  @Autowired
  MapperBootcoin mapperBootcoin;

  @Override
  public Mono<BootcoinDto> create(BootcoinDto bootcoinDto) {
    return  Mono.just(bootcoinDto).map(mapperBootcoin::toBootCoin)
      .flatMap(t -> {
        t.setBalance(BigDecimal.ZERO);
        t.setCreateAt(LocalDate.now());
        return bootcoinRepository.insert(t);
    }).map(mapperBootcoin::toDto);
  }

  @Override
  public Mono<BootcoinDto> findByPhone(String phone) {
    return bootcoinRepository.findByPhone(phone).map(mapperBootcoin::toDto);
  }

  @Override
  public Mono<BootcoinDto> findById(String id) {
    return bootcoinRepository.findById(id).map(mapperBootcoin::toDto);
  }


  @Override
  public Mono<BootcoinDto> addMovements(MovementsDto movementsDto) {
    return bootcoinRepository.findById(movementsDto.getBootcoinId())
      .switchIfEmpty(Mono.error(new InterruptedException("Bootcoin  not found")))
      .flatMap(bootcoin -> {
        if ((bootcoin.getBalance().add(movementsDto.getAmount())).compareTo(BigDecimal.ZERO) < 0) {
          return   Mono.error(new InsufficientBalanceException("There is not enough balance to execute the operation"));
        }

        BootcoinMovements bootcoinMovements = BootcoinMovements.builder()
            .bootcoinId(bootcoin.getId())
            .description(movementsDto.getDescription())
            .amount(movementsDto.getAmount())
            .date(LocalDate.now()).build();

        return Mono.just(bootcoinMovements).flatMap(movementsRepository::insert).flatMap(mov -> {
          bootcoin.setBalance(bootcoin.getBalance().add(movementsDto.getAmount()));
          return  bootcoinRepository.save(bootcoin);
        });

      }).map(mapperBootcoin::toDto);
  }


  @Override
  public Flux<BootcoinDto> findAll() {
    return bootcoinRepository.findAll().map(mapperBootcoin::toDto);
  }
}
