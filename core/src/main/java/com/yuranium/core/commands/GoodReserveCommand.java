package com.yuranium.core.commands;

import com.yuranium.core.KafkaMessage;

import java.io.Serializable;
import java.util.UUID;

public record GoodReserveCommand(
        UUID goodId,

        UUID orderId,

        Long goodQuantity

) implements KafkaMessage, Serializable {}