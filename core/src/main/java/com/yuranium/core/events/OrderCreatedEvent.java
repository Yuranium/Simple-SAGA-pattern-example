package com.yuranium.core.events;

import com.yuranium.core.KafkaMessage;

import java.io.Serializable;
import java.util.UUID;

public record OrderCreatedEvent(
        UUID orderId,

        UUID userId,

        UUID goodId,

        Long goodQuantity

) implements KafkaMessage, Serializable {}