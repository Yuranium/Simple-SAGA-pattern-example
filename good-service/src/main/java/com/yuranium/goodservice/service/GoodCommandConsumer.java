package com.yuranium.goodservice.service;

import com.yuranium.core.commands.CancelGoodReserveCommand;
import com.yuranium.core.commands.GoodReserveCommand;
import com.yuranium.core.events.GoodReserveCancelledEvent;
import com.yuranium.core.events.GoodReserveFailedEvent;
import com.yuranium.core.events.GoodReservedEvent;
import com.yuranium.goodservice.entity.GoodEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@KafkaListener(topics = "${spring.kafka.topic-names.good-command}")
public class GoodCommandConsumer
{
    private final GoodService goodService;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @KafkaHandler
    public void handle(@Payload GoodReserveCommand reserveCommand)
    {
        try
        {
            GoodEntity goodEntity = goodService.reserveGood(reserveCommand);
            kafkaTemplate.send("good-events-topic", new GoodReservedEvent(
                    reserveCommand.goodId(),
                    reserveCommand.orderId(),
                    reserveCommand.goodQuantity(),
                    goodEntity.getGoodPrice()
            ));
        } catch (Exception e)
        {
            kafkaTemplate.send("good-events-topic", new GoodReserveFailedEvent(
                    reserveCommand.goodId(),
                    reserveCommand.orderId(),
                    reserveCommand.goodQuantity()
            ));
        }
    }

    @KafkaHandler
    public void handle(@Payload CancelGoodReserveCommand command)
    {
        goodService.cancelReservation(command);

        kafkaTemplate.send("good-events-topic", new GoodReserveCancelledEvent(
                command.goodId(),
                command.orderId()
        ));
    }
}