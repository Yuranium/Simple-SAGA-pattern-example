package com.yuranium.service;

import com.yuranium.core.events.OrderStatusChangedEvent;
import com.yuranium.enums.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@KafkaListener(topics = "${spring.kafka.topic-names.order}")
public class OrderConsumer
{
    private final OrderService orderService;

    @KafkaHandler
    public void handle(@Payload OrderStatusChangedEvent event)
    {
        orderService.changeOrderStatus(
                event.orderId(),
                OrderStatus.valueOf(event.orderStatus())
        );
    }
}