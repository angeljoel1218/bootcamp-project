package com.nttdata.bootcamp.bootcoinservice.bootcoinservice.application;

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
import java.math.BigDecimal;
import java.time.LocalDate;


@Service
public class BootcoinServiceImpl implements BootcoinService {

  @Autowired
  BootcoinRepository bootcoinRepository;

  @Autowired
  BootcointMovementsRepository movementsRepository;


  @Autowired
  MapperBootcoin mapperBootcoin;

  @Override
  public Mono<BootcoinDto> create(BootcoinDto walletDto) {
    return  Mono.just(walletDto).map(mapperBootcoin::toBootCoin)
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
  public Mono<BootcoinDto> addMovements(MovementsDto movementsDto) {
    return bootcoinRepository.findByPhone(movementsDto.getPhone())
      .switchIfEmpty(Mono.error(new InterruptedException("Bootcoin  not found")))
      .flatMap(bootcoin -> {
        if ((bootcoin.getBalance().add(movementsDto.getAmount())).compareTo(BigDecimal.ZERO) < 0) {
          return   Mono.error(new InterruptedException("You do not have enough balance in your wallet bootcoin"));
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
