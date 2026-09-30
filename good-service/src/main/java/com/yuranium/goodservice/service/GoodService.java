package com.yuranium.goodservice.service;

import com.yuranium.core.commands.CancelGoodReserveCommand;
import com.yuranium.core.commands.GoodCompleteReserveCommand;
import com.yuranium.core.commands.GoodReserveCommand;
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

    @Transactional
    public GoodEntity reserveGood(GoodReserveCommand reserveCommand)
    {
        GoodEntity goodEntity = goodRepository.findById(reserveCommand.goodId())
                .orElseThrow(() -> new RuntimeException("The good with ID=%s was not found"
                        .formatted(reserveCommand.goodId())));

        if (goodEntity.getAvailableQuantity() < reserveCommand.goodQuantity())
            throw new RuntimeException("The good quantity is less than or equal to the good quantity");

        goodEntity.setReservedQuantity(goodEntity.getReservedQuantity() + reserveCommand.goodQuantity());
        return goodRepository.save(goodEntity);
    }

    @Transactional
    public void completeReserveGood(GoodCompleteReserveCommand command)
    {
        GoodEntity goodEntity = goodRepository.findById(command.goodId())
                .orElseThrow(() -> new RuntimeException("The good with ID=%s was not found"
                        .formatted(command.goodId())));

        if (goodEntity.getReservedQuantity() < command.goodQuantity())
            throw new RuntimeException("The reserved good quantity is less than or equal to the good quantity");

        goodEntity.setGoodQuantity(goodEntity.getGoodQuantity() - command.goodQuantity());
        goodEntity.setReservedQuantity(goodEntity.getReservedQuantity() - command.goodQuantity());
        goodRepository.save(goodEntity);
    }

    @Transactional
    public void cancelReservation(CancelGoodReserveCommand command)
    {
        GoodEntity goodEntity = goodRepository.findById(command.goodId())
                .orElseThrow(() -> new RuntimeException("The good with ID=%s was not found"
                        .formatted(command.goodId())));

        goodEntity.setGoodQuantity(goodEntity.getGoodQuantity() + command.goodQuantity());
        goodEntity.setReservedQuantity(goodEntity.getReservedQuantity() - command.goodQuantity());
        goodRepository.save(goodEntity);
    }
}