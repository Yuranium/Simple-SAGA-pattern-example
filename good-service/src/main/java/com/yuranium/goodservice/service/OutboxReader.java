package com.yuranium.goodservice.service;

import com.yuranium.goodservice.entity.OutboxEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class OutboxReader
{
    private final OutboxService outboxService;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Transactional
    @Scheduled(fixedRate = 30_000)
    public void publishEvent()
    {
        Collection<OutboxEntity> events = outboxService.getEvents(100);

        events.forEach(outboxEntity -> {
                    try
                    {
                        kafkaTemplate.send(
                                        outboxEntity.getTopic(),
                                        outboxEntity.getPayload()
                                )
                                .get();
                    } catch (InterruptedException | ExecutionException e)
                    {
                        throw new RuntimeException(e);
                    }
                }
        );
    }
}