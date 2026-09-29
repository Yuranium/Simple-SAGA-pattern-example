package com.yuranium.service;

import com.yuranium.core.commands.RejectOrderCommand;
import com.yuranium.core.events.OrderRejectedEvent;
import com.yuranium.core.events.OrderStatusChangedEvent;
import com.yuranium.enums.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@KafkaListener(topics = "${spring.kafka.topic-names.order-command}")
public class OrderConsumer
{
    private final OrderService orderService;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaHandler
    public void handle(@Payload OrderStatusChangedEvent event)
    {
        orderService.changeOrderStatus(
                event.orderId(),
                OrderStatus.valueOf(event.orderStatus())
        );
    }

    @KafkaHandler
    public void handle(@Payload RejectOrderCommand command)
    {
        orderService.changeOrderStatus(command.orderId(), OrderStatus.REJECTED);
        kafkaTemplate.send("order-events-topic", new OrderRejectedEvent(command.orderId()));

    }
}