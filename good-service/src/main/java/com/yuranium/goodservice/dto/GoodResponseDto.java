package com.yuranium.goodservice.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

public record GoodResponseDto(
        UUID goodId,

        Long goodQuantity,

        BigDecimal goodPrice,

        String goodName

) implements Serializable {}