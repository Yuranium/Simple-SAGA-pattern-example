package com.yuranium.goodservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuranium.goodservice.entity.OutboxEntity;
import com.yuranium.goodservice.repository.OutboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class OutboxService
{
    private final OutboxRepository outboxRepository;

    private final ObjectMapper objectMapper;

    public void createEvent(String topic, Object event)
    {
        try
        {
            var convertedEvent = objectMapper.writeValueAsString(event);
            outboxRepository.save(new OutboxEntity(topic, convertedEvent));
        } catch (JsonProcessingException e)
        {
            e.printStackTrace();
        }
    }

    @Transactional(readOnly = true)
    public Collection<OutboxEntity> getEvents(int size)
    {
        return outboxRepository.findAll(PageRequest.of(0, size))
                .getContent();
    }
}