package com.nttdata.bootcamp.walletservice.application.controller;


import com.nttdata.bootcamp.walletservice.application.WalletService;
import com.nttdata.bootcamp.walletservice.model.Wallet;
import java.math.BigDecimal;
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


@Slf4j
@RefreshScope
@RestController
@RequestMapping("/wallet")
public class WalletController {

  @Autowired
  private WalletService walletService;


  @PostMapping("/create")
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<Wallet> create(@RequestBody @Valid Wallet wallet) {
    return walletService.create(wallet);
  }

  @GetMapping("/get/{phone}")
  public Mono<ResponseEntity<Wallet>> findByPhone(@PathVariable("phone") String phone) {
    return walletService.findByPhone(phone)
      .map(a -> ResponseEntity.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(a))
      .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  @PutMapping("/add-card/{phone}/{cardId}")
  public  Mono<ResponseEntity<Wallet>> addCard(@PathVariable("phone") String phone,
                                               @PathVariable("carNumber") String carNumber,
                                               @PathVariable("cvv") String cvv) {
    return walletService.addCard(phone, carNumber, cvv).map(a -> ResponseEntity.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(a))
      .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  @PutMapping("/set-balance/{phone}/{amount}")
  public  Mono<ResponseEntity<Wallet>> setBalance(@PathVariable("phone") String phone, @PathVariable("amount") BigDecimal amount) {
    return walletService.setBalance(phone, amount).map(a -> ResponseEntity.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(a))
      .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  @GetMapping("/get/all")
  public Flux<ResponseEntity<Wallet>> findAll() {
    return walletService.findAll()
      .map(a -> ResponseEntity.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(a))
      .defaultIfEmpty(ResponseEntity.notFound().build());
  }

}
