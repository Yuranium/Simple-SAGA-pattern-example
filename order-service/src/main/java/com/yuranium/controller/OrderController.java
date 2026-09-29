package com.yuranium.controller;

import com.yuranium.dto.OrderHistoryResponseDto;
import com.yuranium.dto.OrderRequestDto;
import com.yuranium.dto.OrderResponseDto;
import com.yuranium.service.OrderHistoryService;
import com.yuranium.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/order")
public class OrderController
{
    private final OrderService orderService;

    private final OrderHistoryService historyService;

    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(
            @RequestBody OrderRequestDto orderDto
    )
    {
        return new ResponseEntity<>(
                orderService.saveOrder(orderDto),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{orderId}/history")
    public ResponseEntity<Collection<OrderHistoryResponseDto>> orderHistory(
            @PathVariable UUID orderId
    )
    {
        return new ResponseEntity<>(
                historyService.getOrderHistory(orderId),
                HttpStatus.OK
        );
    }
}