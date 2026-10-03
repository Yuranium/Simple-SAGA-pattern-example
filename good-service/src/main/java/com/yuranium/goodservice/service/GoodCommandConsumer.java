package com.yuranium.goodservice.service;

import com.yuranium.core.MessageType;
import com.yuranium.core.commands.CancelGoodReserveCommand;
import com.yuranium.core.commands.GoodCompleteReserveCommand;
import com.yuranium.core.commands.GoodReserveCommand;
import com.yuranium.core.events.GoodCompleteReserveEvent;
import com.yuranium.core.events.GoodFailedCompleteReserveEvent;
import com.yuranium.core.events.GoodReserveFailedEvent;
import com.yuranium.core.events.GoodReservedEvent;
import com.yuranium.goodservice.entity.GoodEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@KafkaListener(topics = "${spring.kafka.topic-names.good-command}")
public class GoodCommandConsumer
{
    private final GoodService goodService;

    private final OutboxService outboxService;

    @Value("${spring.kafka.topic-names.good-event}")
    private String GOODS_TOPIC;

    @KafkaHandler
    @Transactional
    public void handle(@Payload GoodReserveCommand reserveCommand)
    {
        try
        {
            GoodEntity goodEntity = goodService.reserveGood(reserveCommand);
            outboxService.createEvent(
                    GOODS_TOPIC, new GoodReservedEvent(
                            reserveCommand.goodId(),
                            reserveCommand.orderId(),
                            reserveCommand.goodQuantity(),
                            goodEntity.getGoodPrice()
                    ),
                    MessageType.GOOD_RESERVED_EVENT
            );
        } catch (Exception e)
        {
            outboxService.createEvent(
                    GOODS_TOPIC, new GoodReserveFailedEvent(
                            reserveCommand.goodId(),
                            reserveCommand.orderId(),
                            reserveCommand.goodQuantity(),
                            e.getMessage()
                    ),
                    MessageType.GOOD_RESERVE_FAILED_EVENT
            );
        }
    }

    @KafkaHandler
    @Transactional
    public void handle(@Payload GoodCompleteReserveCommand command)
    {
        try
        {
            goodService.completeReserveGood(command);
            outboxService.createEvent(
                    GOODS_TOPIC,
                    new GoodCompleteReserveEvent(command.orderId()),
                    MessageType.GOOD_COMPLETE_RESERVE_EVENT
            );
        } catch (Exception e)
        {
            outboxService.createEvent(
                    GOODS_TOPIC, new GoodFailedCompleteReserveEvent(
                            command.goodId(),
                            command.orderId(),
                            command.goodQuantity(),
                            command.goodPrice()
                    ),
                    MessageType.GOOD_FAILED_COMPLETE_RESERVE_EVENT
            );
        }
    }

    @KafkaHandler
    @Transactional
    public void handle(@Payload CancelGoodReserveCommand command)
    {
        goodService.cancelReservation(command);
    }
}