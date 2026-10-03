package com.yuranium.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuranium.core.KafkaMessage;
import com.yuranium.core.MessageType;
import com.yuranium.entity.OutboxEntity;
import com.yuranium.enums.OutboxStatus;
import com.yuranium.repository.OutboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class OutboxService
{
    private final OutboxRepository outboxRepository;

    private final ObjectMapper objectMapper;

    public void createEvent(String topic, KafkaMessage event, MessageType type)
    {
        try
        {
            var convertedEvent = objectMapper.writeValueAsString(event);
            outboxRepository.save(new OutboxEntity(topic, type , convertedEvent));
        } catch (JsonProcessingException e)
        {
            e.printStackTrace();
        }
    }

    @Transactional(readOnly = true)
    public Collection<OutboxEntity> getEvents(int size)
    {
        return outboxRepository.findNewEvents(Limit.of(size));
    }

    @Transactional
    public void updateStatuses(Collection<Long> outboxIds, OutboxStatus newStatus)
    {
        outboxRepository.updateStatuses(outboxIds, newStatus);
    }
}