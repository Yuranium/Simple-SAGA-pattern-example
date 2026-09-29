package com.yuranium.goodservice.mapper;

import com.yuranium.goodservice.controller.GoodRequestDto;
import com.yuranium.goodservice.dto.GoodResponseDto;
import com.yuranium.goodservice.entity.GoodEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface GoodMapper
{
    GoodEntity toEntity(GoodRequestDto goodRequestDto);

    GoodResponseDto toGoodResponseDto(GoodEntity goodEntity);
}