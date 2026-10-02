package com.yuranium.service;

import com.yuranium.core.commands.*;
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
    private final OrderSagaService sagaService;

    @KafkaHandler
    public void handle(@Payload OrderCreatedEvent event)
    {
        sagaService.handleOrderCreated(event);
    }

    @KafkaHandler
    public void handle(@Payload GoodReservedEvent event)
    {
        sagaService.handleGoodReserved(event);
    }

    @KafkaHandler
    public void handle(@Payload GoodReserveFailedEvent event)
    {
        sagaService.handleGoodReserveFailed(event);
    }

    @KafkaHandler
    public void handle(@Payload PaymentSuccessfulEvent event)
    {
        sagaService.handlePaymentSuccessful(event);
    }

    @KafkaHandler
    public void handle(@Payload PaymentFailedEvent event)
    {
        sagaService.handlePaymentFailed(event);
    }

    @KafkaHandler
    public void handle(@Payload GoodCompleteReserveEvent event)
    {
        sagaService.handleGoodCompleteReserve(event);
    }

    @KafkaHandler
    public void handle(@Payload GoodFailedCompleteReserveEvent event)
    {
        sagaService.handleGoodFailedCompleteReserve(event);
    }

    @KafkaHandler
    public void handle(@Payload GoodReserveCancelledEvent event)
    {
        sagaService.handleGoodReserveCancelled(event);
    }

    @KafkaHandler
    public void handle(@Payload OrderRejectedEvent event)
    {
        sagaService.handleOrderRejected(event);
    }
}