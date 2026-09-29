package com.yuranium.entity;

import com.yuranium.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "order_history")
@NoArgsConstructor
@AllArgsConstructor
public class OrderHistoryEntity
{
    @Id
    private UUID orderHistoryId;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    private OrderEntity order;
}