package com.nttdata.bootcamp.bootcoinservice.bootcoinservice.application.controller;


import com.nttdata.bootcamp.bootcoinservice.bootcoinservice.application.BootcoinService;
import com.nttdata.bootcamp.bootcoinservice.bootcoinservice.model.dto.BootcoinDto;
import com.nttdata.bootcamp.bootcoinservice.bootcoinservice.model.dto.MovementsDto;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


/**
 * @since 2022
 */
@Slf4j
@RefreshScope
@RestController
@RequestMapping("/bootcoin")
public class BootcoinController {

  @Autowired
  private BootcoinService bootcoinService;


  @PostMapping("/create")
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<BootcoinDto> create(@RequestBody @Valid BootcoinDto bootcoinDto) {
    return bootcoinService.create(bootcoinDto);
  }

  @GetMapping("/{id}")
  public Mono<ResponseEntity<BootcoinDto>> findByPhone(@PathVariable("id") String id) {
    return bootcoinService.findById(id)
      .map(a -> ResponseEntity.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(a))
      .defaultIfEmpty(ResponseEntity.notFound().build());
  }


  @PostMapping("/transaction")
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<BootcoinDto> addMovements(@RequestBody @Valid MovementsDto movementsDto) {
    return bootcoinService.addMovements(movementsDto);
  }

  @GetMapping("/get/all")
  public Flux<BootcoinDto> findAll() {
    return bootcoinService.findAll();
  }

}
