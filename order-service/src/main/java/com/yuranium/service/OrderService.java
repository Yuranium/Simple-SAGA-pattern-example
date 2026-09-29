package com.yuranium.service;

import com.yuranium.dto.OrderRequestDto;
import com.yuranium.dto.OrderResponseDto;
import com.yuranium.entity.OrderEntity;
import com.yuranium.enums.OrderStatus;
import com.yuranium.mapper.OrderMapper;
import com.yuranium.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService
{
    private final OrderRepository orderRepository;

    private final OrderHistoryService historyService;

    private final OrderMapper orderMapper;

    @Transactional
    public OrderResponseDto saveOrder(OrderRequestDto orderDto)
    {
        OrderEntity orderEntity = orderMapper.toOrderEntity(orderDto);
        orderEntity.setOrderId(UUID.randomUUID());
        OrderEntity saved = orderRepository.save(orderEntity);
        historyService.addNewHistory(saved, OrderStatus.CREATED);
        return orderMapper.toOrderResponseDto(saved);
    }
}