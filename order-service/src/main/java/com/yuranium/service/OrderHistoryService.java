package com.yuranium.service;

import com.yuranium.dto.OrderHistoryResponseDto;
import com.yuranium.entity.OrderEntity;
import com.yuranium.entity.OrderHistoryEntity;
import com.yuranium.enums.OrderStatus;
import com.yuranium.mapper.OrderMapper;
import com.yuranium.repository.OrderHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderHistoryService
{
    private final OrderHistoryRepository historyRepository;

    private final OrderMapper orderMapper;

    @Transactional
    public void addNewHistory(OrderEntity order, OrderStatus orderStatus)
    {
        historyRepository.save(
                new OrderHistoryEntity(UUID.randomUUID(), orderStatus, order)
        );
    }

    @Transactional
    public Collection<OrderHistoryResponseDto> getOrderHistory(UUID orderId)
    {
        return orderMapper.toHistoryResponseDto(
                historyRepository.findByOrderId(orderId)
        );
    }
}