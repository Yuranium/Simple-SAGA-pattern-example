package com.yuranium.dto;

import com.yuranium.enums.OrderStatus;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

public record OrderHistoryResponseDto(
        UUID historyId,

        UUID orderId,

        OrderStatus status,

        Instant createdAt

) implements Serializable {}