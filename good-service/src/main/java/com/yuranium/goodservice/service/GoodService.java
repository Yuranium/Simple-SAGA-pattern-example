package com.yuranium.goodservice.service;

import com.yuranium.goodservice.controller.GoodRequestDto;
import com.yuranium.goodservice.dto.GoodResponseDto;
import com.yuranium.goodservice.entity.GoodEntity;
import com.yuranium.goodservice.mapper.GoodMapper;
import com.yuranium.goodservice.repository.GoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GoodService
{
    private final GoodRepository goodRepository;

    private final GoodMapper goodMapper;

    @Transactional
    public GoodResponseDto saveGood(GoodRequestDto goodRequestDto)
    {
        GoodEntity prepared = goodMapper.toEntity(goodRequestDto);
        prepared.setGoodId(UUID.randomUUID());
        return goodMapper.toGoodResponseDto(goodRepository.save(prepared));
    }

    @Transactional(readOnly = true)
    public Page<GoodResponseDto> getAll(Pageable pageable)
    {
        return goodRepository.findAll(pageable)
                .map(goodMapper::toGoodResponseDto);
    }
}