package com.nttdata.bootcamp.adbootcoinservice.application.service.impl;

import com.nttdata.bootcamp.adbootcoinservice.application.exception.AdvertException;
import com.nttdata.bootcamp.adbootcoinservice.application.mapper.MapperAdvert;
import com.nttdata.bootcamp.adbootcoinservice.application.mapper.MapperPayOrder;
import com.nttdata.bootcamp.adbootcoinservice.application.service.AdvertService;
import com.nttdata.bootcamp.adbootcoinservice.domain.constant.StateAdvert;
import com.nttdata.bootcamp.adbootcoinservice.domain.dto.AdvertDto;
import com.nttdata.bootcamp.adbootcoinservice.domain.dto.PayOrderDto;
import com.nttdata.bootcamp.adbootcoinservice.domain.dto.RequestPurchaseDto;
import com.nttdata.bootcamp.adbootcoinservice.domain.model.PayOrder;
import com.nttdata.bootcamp.adbootcoinservice.infrastructure.AdvertRepository;
import com.nttdata.bootcamp.adbootcoinservice.infrastructure.PayOrderRepository;
import com.nttdata.bootcamp.adbootcoinservice.infrastructure.events.ProducerPurchaseRequest;
import com.nttdata.bootcamp.adbootcoinservice.infrastructure.webclient.MasterDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Date;



/**
 *
 * @since 2022
 */
@Service
public class AdvertServiceImpl implements AdvertService {
    @Autowired
    AdvertRepository advertRepository;
    @Autowired
    PayOrderRepository payOrderRepository;
    @Autowired
    MasterDataService masterDataService;
    @Autowired
    ProducerPurchaseRequest producerPurchaseRequest;
    @Autowired
    MapperAdvert mapperAdvert;
    @Autowired
    MapperPayOrder mapperPayOrder;

    @Override
    public Mono<AdvertDto> postAd(AdvertDto advertDto) {
        advertDto.setState(StateAdvert.ACTIVE);
        advertDto.setCreatedAt(new Date());
        return Mono.just(advertDto)
                .map(mapperAdvert::toAdvert)
                .flatMap(advertRepository::insert)
                .map(mapperAdvert::toDto);

    }

    @Override
    public Flux<AdvertDto> listAd() {
        return advertRepository
                .findByState(StateAdvert.ACTIVE)
                .map(mapperAdvert::toDto);
    }

    @Override
    public Mono<PayOrderDto> requestPurchase(RequestPurchaseDto requestPurchaseDto) {
        return advertRepository
                .findById(requestPurchaseDto.getAdvertId())
                .switchIfEmpty(Mono.error(new AdvertException("Ad no found")))
                .doOnNext(advert -> {
                    if (requestPurchaseDto.getAmount().compareTo(advert.getMinAmount()) < 0) {
                        throw new AdvertException("The amount must be at least "
                          + advert.getMinAmount());
                    }
                })
                .flatMap(advert -> masterDataService.getExchangeRate()
                        .flatMap(exchangeRate -> {
                            BigDecimal amountPay = requestPurchaseDto.getAmount()
                              .multiply(exchangeRate.getPrice());
                            PayOrder payOrder = new PayOrder();
                            payOrder.setAmount(requestPurchaseDto.getAmount());
                            payOrder.setAmountPay(amountPay);
                            payOrder.setBuyerWalletId(requestPurchaseDto.getBuyerWalletId());
                            payOrder.setSellerWalletId(advert.getWalletId());
                            payOrder.setExchangeRate(exchangeRate.getPrice());
                            payOrder.setMethodPayment(advert.getMethodPayment());
                            payOrder.setAdvertId(advert.getId());
                            payOrder.setCreatedAt(new Date());
                            return Mono.just(payOrder)
                                    .flatMap(payOrderRepository::insert)
                                    .flatMap(order -> {
                                        advert.setState(StateAdvert.PENDING);
                                        return advertRepository.save(advert)
                                                .doOnNext(advert1 -> producerPurchaseRequest.sendMessage(order))
                                                .map(a -> order);
                                    })
                                    .map(mapperPayOrder::toDto);
                        }));
    }

  @Override
  public Mono<AdvertDto> acceptPurchase(String id) {
    return advertRepository.findById(id)
      .doOnNext(t->t.setState(StateAdvert.CLOSED))
      .flatMap(advertRepository::save)
      .map(mapperAdvert::toDto);
  }
}
