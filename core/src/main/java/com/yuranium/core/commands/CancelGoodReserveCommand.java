package com.yuranium.core.commands;

import com.yuranium.core.KafkaMessage;

import java.io.Serializable;
import java.util.UUID;

public record CancelGoodReserveCommand(
        UUID goodId,

        UUID orderId,

        Long goodQuantity

) implements KafkaMessage, Serializable {}