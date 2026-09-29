package com.yuranium.dto;

import com.yuranium.enums.OrderStatus;

import java.io.Serializable;
import java.util.UUID;

public record OrderResponseDto(
        UUID orderId,

        UUID userId,

        UUID goodId,

        Long goodQuantity,

        OrderStatus status

) implements Serializable {}