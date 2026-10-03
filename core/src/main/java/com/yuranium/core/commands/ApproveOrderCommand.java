package com.yuranium.core.commands;

import com.yuranium.core.KafkaMessage;

import java.io.Serializable;
import java.util.UUID;

public record ApproveOrderCommand(
        UUID orderId

) implements KafkaMessage, Serializable {}