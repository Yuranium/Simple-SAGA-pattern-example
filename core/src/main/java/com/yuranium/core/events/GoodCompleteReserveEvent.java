package com.yuranium.core.events;

import java.io.Serializable;
import java.util.UUID;

public record GoodCompleteReserveEvent(
        UUID orderId

) implements Serializable {}