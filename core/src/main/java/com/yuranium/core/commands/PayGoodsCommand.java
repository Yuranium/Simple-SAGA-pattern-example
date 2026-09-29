package com.yuranium.core.commands;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

public record PayGoodsCommand(
        UUID goodId,

        UUID orderId,

        Long goodQuantity,

        BigDecimal goodPrice

) implements Serializable {}