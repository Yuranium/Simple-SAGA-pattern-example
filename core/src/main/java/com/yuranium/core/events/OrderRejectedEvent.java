package com.yuranium.core.events;

import java.io.Serializable;
import java.util.UUID;

public record OrderRejectedEvent(
        UUID orderId

) implements Serializable {}