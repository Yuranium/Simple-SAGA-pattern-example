package com.yuranium.goodservice.dto;

import com.yuranium.goodservice.enums.GoodStatus;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

public record GoodResponseDto(
        UUID goodId,

        Long goodQuantity,

        Long availableQuantity,

        BigDecimal goodPrice,

        String goodName,

        GoodStatus status

) implements Serializable {}