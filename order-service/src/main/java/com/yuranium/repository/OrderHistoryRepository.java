package com.yuranium.repository;

import com.yuranium.entity.OrderHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.UUID;

@Repository
public interface OrderHistoryRepository extends JpaRepository<OrderHistoryEntity, UUID>
{
    @Query(value = "FROM OrderHistoryEntity WHERE order.orderId = :orderId")
    Collection<OrderHistoryEntity> findByOrderId(UUID orderId);
}