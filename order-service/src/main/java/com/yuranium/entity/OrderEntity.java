package com.yuranium.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "orders")
@NoArgsConstructor
@AllArgsConstructor
public class OrderEntity
{
    @Id
    private UUID orderId;

    private UUID userId;

    private UUID goodId;

    private Long goodQuantity;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "order")
    private Set<OrderHistoryEntity> orderHistory;
}