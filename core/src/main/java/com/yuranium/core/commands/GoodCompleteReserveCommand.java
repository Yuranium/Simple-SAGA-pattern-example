package com.yuranium.core.commands;

import com.yuranium.core.KafkaMessage;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

public record GoodCompleteReserveCommand(
        UUID goodId,

        UUID orderId,

        Long goodQuantity,

        BigDecimal goodPrice

) implements KafkaMessage, Serializable {}