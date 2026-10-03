package com.yuranium.goodservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuranium.core.MessageType;
import com.yuranium.core.events.*;
import com.yuranium.goodservice.entity.OutboxEntity;
import com.yuranium.goodservice.enums.OutboxStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class OutboxReader
{
    private final OutboxService outboxService;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private final ObjectMapper objectMapper;

    @Transactional
    @Scheduled(fixedRate = 30_000)
    public void publishEvent()
    {
        Collection<OutboxEntity> events = outboxService.getEvents(100);
        List<Long> idsEvents = new ArrayList<>();

        for (OutboxEntity event : events)
        {
            try
            {
                var dto = objectMapper.readValue(
                        event.getPayload(),
                        determineClass(event.getEventType())
                );

                kafkaTemplate.send(event.getTopic(), dto)
                        .get();
                idsEvents.add(event.getOutboxId());
            } catch (InterruptedException | ExecutionException | JsonProcessingException e)
            {
                e.printStackTrace();
            }
        }

        outboxService.updateStatuses(idsEvents, OutboxStatus.SENT);
    }

    private Class<?> determineClass(MessageType className)
    {

        return switch (className)
        {
            case GOOD_COMPLETE_RESERVE_EVENT -> GoodCompleteReserveEvent.class;

            case GOOD_FAILED_COMPLETE_RESERVE_EVENT -> GoodFailedCompleteReserveEvent.class;

            case GOOD_RESERVE_CANCELLED_EVENT -> GoodReserveCancelledEvent.class;

            case GOOD_RESERVED_EVENT -> GoodReservedEvent.class;

            case GOOD_RESERVE_FAILED_EVENT -> GoodReserveFailedEvent.class;

            case ORDER_CREATED_EVENT -> OrderCreatedEvent.class;

            case ORDER_REJECTED_EVENT -> OrderRejectedEvent.class;

            case ORDER_STATUS_CHANGED_EVENT -> OrderStatusChangedEvent.class;

            case PAYMENT_FAILED_EVENT -> PaymentFailedEvent.class;

            case PAYMENT_SUCCESSFUL_EVENT -> PaymentSuccessfulEvent.class;

            default -> throw new IllegalArgumentException("Unknown event type " + className);
        };

    }
}