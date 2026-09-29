package com.yuranium.core.commands;

import java.io.Serializable;
import java.util.UUID;

public record GoodReserveCommand(
        UUID goodId,

        UUID orderId,

        Long goodQuantity

) implements Serializable {}