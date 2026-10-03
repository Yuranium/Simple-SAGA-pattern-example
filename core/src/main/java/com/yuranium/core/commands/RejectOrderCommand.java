package com.yuranium.core.commands;

import com.yuranium.core.KafkaMessage;

import java.io.Serializable;
import java.util.UUID;

public record RejectOrderCommand(
        UUID orderId

) implements KafkaMessage, Serializable {}