package com.yuranium.goodservice.repository;

import com.yuranium.goodservice.entity.OutboxEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OutboxRepository  extends JpaRepository<OutboxEntity, Long> {}