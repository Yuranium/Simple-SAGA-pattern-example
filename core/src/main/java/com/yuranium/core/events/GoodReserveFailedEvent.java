package com.yuranium.core.events;

import com.yuranium.core.KafkaMessage;

import java.io.Serializable;
import java.util.UUID;

public record GoodReserveFailedEvent(
        UUID goodId,

        UUID orderId,

        Long goodQuantity,

        String reason

) implements KafkaMessage, Serializable {}