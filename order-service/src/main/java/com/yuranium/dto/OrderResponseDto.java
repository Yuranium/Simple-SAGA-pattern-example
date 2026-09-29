package com.yuranium.dto;

import java.io.Serializable;
import java.util.UUID;

public record OrderResponseDto(
        UUID orderId,

        UUID userId,

        UUID goodId,

        Long goodQuantity

) implements Serializable {}