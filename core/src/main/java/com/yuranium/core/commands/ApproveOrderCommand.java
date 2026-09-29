package com.yuranium.core.commands;

import java.io.Serializable;
import java.util.UUID;

public record ApproveOrderCommand(
        UUID orderId

) implements Serializable {}