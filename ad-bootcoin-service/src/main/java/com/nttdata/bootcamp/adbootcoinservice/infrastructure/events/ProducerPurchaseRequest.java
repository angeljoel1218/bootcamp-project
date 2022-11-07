package com.nttdata.bootcamp.adbootcoinservice.infrastructure.events;

import com.nttdata.bootcamp.adbootcoinservice.domain.model.PayOrder;
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
public class ProducerPurchaseRequest {
    private final KafkaTemplate<String, PayOrder> kafkaTemplate;

    @Value(value = "${kafka.topic.bootcoin.purchase.request}")
    private String topic;

    public void sendMessage(PayOrder payOrder) {
        ListenableFuture<SendResult<String, PayOrder>> future = kafkaTemplate.send(this.topic, payOrder);

        future.addCallback(new ListenableFutureCallback<SendResult<String, PayOrder>>() {
            @Override
            public void onSuccess(SendResult<String, PayOrder> result) {
                log.info("Message {} has been sent ", payOrder);
            }

            @Override
            public void onFailure(Throwable ex) {
                log.error("Something went wrong with the balanceModel {} ", payOrder);
            }
        });
    }
}
