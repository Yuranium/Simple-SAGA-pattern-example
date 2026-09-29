package com.yuranium.dto;

import com.yuranium.enums.OrderStatus;

import java.io.Serializable;
import java.util.UUID;

public record OrderHistoryResponseDto(
        UUID orderHistoryId,

        UUID orderId,

        OrderStatus orderStatus

) implements Serializable {}