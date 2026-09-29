package com.yuranium.service;

import com.yuranium.core.events.OrderCreatedEvent;
import com.yuranium.dto.OrderRequestDto;
import com.yuranium.dto.OrderResponseDto;
import com.yuranium.entity.OrderEntity;
import com.yuranium.enums.OrderStatus;
import com.yuranium.mapper.OrderMapper;
import com.yuranium.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService
{
    private final OrderRepository orderRepository;

    private final OrderMapper orderMapper;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Transactional
    public OrderResponseDto saveOrder(OrderRequestDto orderDto)
    {
        OrderEntity orderEntity = orderMapper.toOrderEntity(orderDto);
        orderEntity.setOrderId(UUID.randomUUID());
        orderEntity.setStatus(OrderStatus.CREATED);
        OrderEntity saved = orderRepository.save(orderEntity);

        kafkaTemplate.send("order-events-topic", new OrderCreatedEvent(
                saved.getOrderId(),
                saved.getUserId(),
                saved.getGoodId(),
                saved.getGoodQuantity()
        ));
        return orderMapper.toOrderResponseDto(saved);
    }
}