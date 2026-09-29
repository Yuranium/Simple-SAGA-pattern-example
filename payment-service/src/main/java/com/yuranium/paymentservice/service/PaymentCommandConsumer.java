package com.yuranium.paymentservice.service;

import com.yuranium.core.commands.PayGoodsCommand;
import com.yuranium.core.events.GoodPayedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
@KafkaListener(topics = "${spring.kafka.topic-names.payment}")
public class PaymentCommandConsumer
{
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaHandler
    public void handle(@Payload PayGoodsCommand event)
    {
        log.info("Received Payment Command: {}", event);
        kafkaTemplate.send("payment-events-topic", new GoodPayedEvent(
                event.goodId(),
                event.orderId(),
                event.goodQuantity(),
                event.goodPrice()
        ));
    }
}