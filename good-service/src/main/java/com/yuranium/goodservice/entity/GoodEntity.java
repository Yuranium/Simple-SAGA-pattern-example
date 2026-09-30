package com.yuranium.goodservice.entity;

import com.yuranium.goodservice.enums.GoodStatus;
import jakarta.persistence.*;
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

    private Long reservedQuantity;

    private BigDecimal goodPrice;

    private String goodName;

    @PrePersist
    private void prePersist()
    {
        if (reservedQuantity == null)
            this.reservedQuantity = 0L;
    }

    @Transient
    public GoodStatus getStatus()
    {
        if (getAvailableQuantity() > 0)
            return GoodStatus.AVAILABLE;

        else if (reservedQuantity > 0 && getAvailableQuantity() == 0)
            return GoodStatus.RESERVED;

        return GoodStatus.OUT_OF_STOCK;
    }

    @Transient
    public Long getAvailableQuantity()
    {
        return goodQuantity - reservedQuantity;
    }
}