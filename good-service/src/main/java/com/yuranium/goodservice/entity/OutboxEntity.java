package com.yuranium.goodservice.entity;

import com.yuranium.core.MessageType;
import com.yuranium.goodservice.enums.OutboxStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "good_outbox")
@NoArgsConstructor
@AllArgsConstructor
public class OutboxEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long outboxId;

    private String topic;

    @Enumerated(EnumType.STRING)
    private MessageType eventType;

    private String payload;

    @Enumerated(EnumType.STRING)
    private OutboxStatus status = OutboxStatus.NEW;

    @CreationTimestamp
    private Instant savedAt;

    public OutboxEntity(String topic, MessageType eventType, String payload)
    {
        this.topic = topic;
        this.eventType = eventType;
        this.payload = payload;
    }
}