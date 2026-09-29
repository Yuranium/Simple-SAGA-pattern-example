package com.yuranium.core.commands;

import java.io.Serializable;
import java.util.UUID;

public record RejectOrderCommand(
        UUID orderId

) implements Serializable {}
