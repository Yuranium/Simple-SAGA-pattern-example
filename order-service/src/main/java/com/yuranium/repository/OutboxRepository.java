package com.yuranium.repository;

import com.yuranium.entity.OutboxEntity;
import com.yuranium.enums.OutboxStatus;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface OutboxRepository extends JpaRepository<OutboxEntity, Long>
{
    @Query("FROM OutboxEntity WHERE status = 'NEW'")
    Collection<OutboxEntity> findNewEvents(Limit limit);

    @Modifying
    @Query("UPDATE OutboxEntity SET status = :newStatus WHERE outboxId IN(:outboxIds)")
    void updateStatuses(Collection<Long> outboxIds, OutboxStatus newStatus);
}