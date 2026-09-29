package com.yuranium.goodservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "good")
@NoArgsConstructor
@AllArgsConstructor
public class GoodEntity
{
    @Id
    private UUID goodId;

    private Long goodQuantity;

    private BigDecimal goodPrice;

    private String goodName;
}