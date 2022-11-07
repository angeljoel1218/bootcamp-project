package com.nttdata.bootcamp.exchangebootcoinservice.infrastructure.events;

import com.nttdata.bootcamp.exchangebootcoinservice.domain.dto.RequestBootcoin;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Component
@Slf4j
@RequiredArgsConstructor
public class ProducerBootcoinBalance {
    private final KafkaTemplate<String, RequestBootcoin> kafkaTemplate;

    @Value(value = "${kafka.topic.bootcoin.balance.name}")
    private String topic;

    public void sendMessage(RequestBootcoin requestBootcoin) {
        ListenableFuture<SendResult<String, RequestBootcoin>> future = kafkaTemplate.send(this.topic, requestBootcoin);

        future.addCallback(new ListenableFutureCallback<SendResult<String, RequestBootcoin>>() {
            @Override
            public void onSuccess(SendResult<String, RequestBootcoin> result) {
                log.info("Message {} has been sent ", requestBootcoin);
            }

            @Override
            public void onFailure(Throwable ex) {
                log.error("Something went wrong with the balanceModel {} ", requestBootcoin);
            }
        });
    }
}
