package com.yuranium.goodservice.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public record GoodRequestDto(
        Long goodQuantity,

        BigDecimal goodPrice,

        String goodName

) implements Serializable {}
