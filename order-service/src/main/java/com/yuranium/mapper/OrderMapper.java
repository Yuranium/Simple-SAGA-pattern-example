package com.yuranium.mapper;

import com.yuranium.dto.OrderHistoryResponseDto;
import com.yuranium.dto.OrderRequestDto;
import com.yuranium.dto.OrderResponseDto;
import com.yuranium.entity.OrderEntity;
import com.yuranium.entity.OrderHistoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.Collection;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderMapper
{
    OrderResponseDto toOrderResponseDto(OrderEntity order);

    OrderEntity toOrderEntity(OrderRequestDto responseDto);

    Collection<OrderHistoryResponseDto> toHistoryResponseDto(Collection<OrderHistoryEntity> orderHistory);
}