package com.yuranium.core.events;

import com.yuranium.core.KafkaMessage;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

public record GoodReservedEvent(
        UUID goodId,

        UUID orderId,

        Long goodQuantity,

        BigDecimal goodPrice

) implements KafkaMessage, Serializable {}