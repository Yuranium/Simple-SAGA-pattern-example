package com.yuranium.service;

import com.yuranium.core.commands.GoodReserveCommand;
import com.yuranium.core.events.OrderCreatedEvent;
import com.yuranium.enums.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@KafkaListener(topics = "${spring.kafka.topic-names.order}")
public class OrderSaga
{
    private final OrderHistoryService historyService;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaHandler
    public void handle(@Payload OrderCreatedEvent event)
    {
        kafkaTemplate.send("good-command-topic", new GoodReserveCommand(
                event.goodId(),
                event.orderId(),
                event.goodQuantity())
        );

        historyService.addNewHistory(event.orderId(), OrderStatus.CREATED);
    }
}