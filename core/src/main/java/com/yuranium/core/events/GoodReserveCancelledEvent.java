package com.yuranium.core.events;

import java.io.Serializable;
import java.util.UUID;

public record GoodReserveCancelledEvent(
        UUID goodId,

        UUID orderId

) implements Serializable {}