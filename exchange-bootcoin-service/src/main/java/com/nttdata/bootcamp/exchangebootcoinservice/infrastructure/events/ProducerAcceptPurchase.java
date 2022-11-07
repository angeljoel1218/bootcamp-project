package com.nttdata.bootcamp.exchangebootcoinservice.infrastructure.events;

import com.nttdata.bootcamp.exchangebootcoinservice.domain.dto.TransactionBootcoinDto;
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
public class ProducerAcceptPurchase {
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value(value = "${kafka.topic.accept.purchase}")
    private String topic;

    public void sendMessage(String accept) {
        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(this.topic, accept);

        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            @Override
            public void onSuccess(SendResult<String, String> result) {
                log.info("Message {} has been sent ", accept);
            }

            @Override
            public void onFailure(Throwable ex) {
                log.error("Something went wrong with the balanceModel {} ", accept);
            }
        });
    }
}
