package com.nttdata.bootcamp.adbootcoinservice.application.controller;

import com.nttdata.bootcamp.adbootcoinservice.application.service.AdvertService;
import com.nttdata.bootcamp.adbootcoinservice.domain.dto.AdvertDto;
import com.nttdata.bootcamp.adbootcoinservice.domain.dto.PayOrderDto;
import com.nttdata.bootcamp.adbootcoinservice.domain.dto.RequestPurchaseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *
 * @since 2022
 */
@RefreshScope
@RestController
public class AdvertController {
    @Autowired
    AdvertService advertService;

    @PostMapping("ad/post")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<AdvertDto> postAd(@RequestBody AdvertDto advertDto){
        return advertService.postAd(advertDto);
    }

    @GetMapping("ad/all")
    public Flux<AdvertDto> listAd(){
        return advertService.listAd();
    }

    @PostMapping("ad/request-purchase")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<PayOrderDto> requestPurchase(@RequestBody RequestPurchaseDto requestPurchaseDto){
        return advertService.requestPurchase(requestPurchaseDto);
    }
}
