package com.yuranium.service;

import com.yuranium.core.commands.CancelGoodReserveCommand;
import com.yuranium.core.commands.GoodReserveCommand;
import com.yuranium.core.commands.PayGoodsCommand;
import com.yuranium.core.commands.RejectOrderCommand;
import com.yuranium.core.events.*;
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

        kafkaTemplate.send("order-command-topic", new OrderStatusChangedEvent(
                event.orderId(),
                OrderStatus.RESERVED.name()
        ));

        historyService.addNewHistory(event.orderId(), OrderStatus.RESERVED);
    }

    @KafkaHandler
    public void handle(@Payload GoodReserveFailedEvent event)
    {
        kafkaTemplate.send("order-command-topic", new RejectOrderCommand(
                event.orderId()
        ));
    }

    @KafkaHandler
    public void handle(@Payload PaymentSuccessfulEvent event)
    {
        kafkaTemplate.send("order-command-topic", new OrderStatusChangedEvent(
                event.orderId(),
                OrderStatus.APPROVED.name()
        ));

        historyService.addNewHistory(event.orderId(), OrderStatus.APPROVED);
        //kafkaTemplate.send("payment-command-topic", new ApproveOrderCommand(event.orderId()));
    }

    @KafkaHandler
    public void handle(@Payload PaymentFailedEvent event)
    {
        kafkaTemplate.send("good-command-topic", new CancelGoodReserveCommand(
                event.goodId(),
                event.orderId(),
                event.goodQuantity()
        ));
    }

    @KafkaHandler
    public void handle(@Payload GoodReserveCancelledEvent event)
    {
        kafkaTemplate.send("order-command-topic",
                new RejectOrderCommand(event.orderId()));
    }

    @KafkaHandler
    public void handle(@Payload OrderRejectedEvent event)
    {
        historyService.addNewHistory(event.orderId(), OrderStatus.REJECTED);
    }
}