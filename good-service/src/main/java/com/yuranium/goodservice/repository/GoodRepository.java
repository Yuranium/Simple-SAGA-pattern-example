package com.yuranium.goodservice.repository;

import com.yuranium.goodservice.entity.GoodEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GoodRepository extends JpaRepository<GoodEntity, UUID> {}
