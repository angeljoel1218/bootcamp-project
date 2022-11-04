package com.nttdata.bootcamp.wallettransactionservice.infrastructure.producer;

import com.nttdata.bootcamp.wallettransactionservice.model.dto.TransactionRequestDto;
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
public class BalanceProducer {
    private final KafkaTemplate<String, TransactionRequestDto> kafkaTemplate;

    @Value(value = "${kafka.topic.balance.name}")
    private String topic;

    public void sendMessage(TransactionRequestDto transactionRequestDto) {
        System.out.println("============="+transactionRequestDto.getSourceNumberCell());
        ListenableFuture<SendResult<String, TransactionRequestDto>> future = kafkaTemplate.send(this.topic, transactionRequestDto);

        future.addCallback(new ListenableFutureCallback<SendResult<String, TransactionRequestDto>>() {
            @Override
            public void onSuccess(SendResult<String, TransactionRequestDto> result) {
                log.info("Message {} has been sent ", transactionRequestDto);
            }

            @Override
            public void onFailure(Throwable ex) {
                log.error("Something went wrong with the balanceModel {} ", transactionRequestDto);
            }
        });
    }
}
