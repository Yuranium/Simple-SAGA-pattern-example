package com.yuranium.goodservice.entity;

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
@Table(name = "outbox")
@NoArgsConstructor
@AllArgsConstructor
public class OutboxEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long outboxId;

    private String topic;

    private String payload;

    @CreationTimestamp
    private Instant savedAt;

    public OutboxEntity(String topic, String payload)
    {
        this.topic = topic;
        this.payload = payload;
    }
}