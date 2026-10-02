package com.yuranium.service;

import com.yuranium.entity.OutboxEntity;
import com.yuranium.enums.OutboxStatus;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
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
                var record = new ProducerRecord<String, Object>(
                        event.getTopic(),
                        event.getPayload()
                );

                record.headers()
                        .add("__TypeId__",
                                event.getEventType()
                                        .getBytes(StandardCharsets.UTF_8)
                        );

                System.out.println(record);
                kafkaTemplate.send(record)
                        .get();

                idsEvents.add(event.getOutboxId());
            } catch (InterruptedException | ExecutionException e)
            {
                e.printStackTrace();
            }
        }

        outboxService.updateStatuses(idsEvents, OutboxStatus.SENT);
    }
}