package com.nttdata.bootcamp.adbootcoinservice.application.service;

import com.nttdata.bootcamp.adbootcoinservice.domain.dto.AdvertDto;
import com.nttdata.bootcamp.adbootcoinservice.domain.dto.PayOrderDto;
import com.nttdata.bootcamp.adbootcoinservice.domain.dto.RequestPurchaseDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *
 * @since 2022
 */

public interface AdvertService {
    Mono<AdvertDto> postAd(AdvertDto advertDto);
    Flux<AdvertDto> listAd();
    Mono<PayOrderDto> requestPurchase(RequestPurchaseDto requestPurchaseDto);
    Mono<AdvertDto> acceptPurchase(String id);
}
