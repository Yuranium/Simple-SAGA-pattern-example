package com.yuranium.core.events;

import com.yuranium.core.KafkaMessage;

import java.io.Serializable;
import java.util.UUID;

public record OrderRejectedEvent(
        UUID orderId

) implements KafkaMessage, Serializable {}