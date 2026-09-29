package com.yuranium.entity;

import com.yuranium.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
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
    private UUID historyId;

    private UUID orderId;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @CreationTimestamp
    private Instant createdAt;
}