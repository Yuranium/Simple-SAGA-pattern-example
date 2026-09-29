package com.yuranium.dto;

import java.io.Serializable;
import java.util.UUID;

public record OrderRequestDto(
        UUID userId,

        UUID goodId,

        Long goodQuantity

) implements Serializable {}