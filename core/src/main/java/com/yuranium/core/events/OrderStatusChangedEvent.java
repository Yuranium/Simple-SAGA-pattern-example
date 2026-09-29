package com.yuranium.core.events;

import java.io.Serializable;
import java.util.UUID;

public record OrderStatusChangedEvent(
        UUID orderId,

        String orderStatus

) implements Serializable {}