package com.yuranium.service;

import com.yuranium.core.commands.ApproveOrderCommand;
import com.yuranium.core.commands.GoodReserveCommand;
import com.yuranium.core.commands.PayGoodsCommand;
import com.yuranium.core.events.GoodPayedEvent;
import com.yuranium.core.events.GoodReserveFailedEvent;
import com.yuranium.core.events.GoodReservedEvent;
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
@KafkaListener(topics = {
        "${spring.kafka.topic-names.order}",
        "${spring.kafka.topic-names.good}",
        "${spring.kafka.topic-names.payment}"
})
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

    @KafkaHandler
    public void handle(@Payload GoodReservedEvent event)
    {
        kafkaTemplate.send("payment-command-topic", new PayGoodsCommand(
                event.goodId(),
                event.orderId(),
                event.goodQuantity(),
                event.goodPrice()
        ));

        historyService.addNewHistory(event.orderId(), OrderStatus.RESERVED);
    }

    @KafkaHandler
    public void handle(@Payload GoodReserveFailedEvent event)
    {
        historyService.addNewHistory(event.orderId(), OrderStatus.REJECTED);
    }

    @KafkaHandler
    public void handle(@Payload GoodPayedEvent event)
    {
        kafkaTemplate.send("payment-command-topic", new ApproveOrderCommand(event.orderId()));
        historyService.addNewHistory(event.orderId(), OrderStatus.APPROVED);
    }
}