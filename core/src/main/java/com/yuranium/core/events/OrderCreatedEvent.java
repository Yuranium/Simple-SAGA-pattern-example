package com.yuranium.core.events;

import java.io.Serializable;
import java.util.UUID;

public record OrderCreatedEvent(
        UUID orderId,

        UUID userId,

        UUID goodId,

        Long goodQuantity

) implements Serializable {}