package com.yuranium.goodservice.controller;

import com.yuranium.goodservice.dto.GoodResponseDto;
import com.yuranium.goodservice.service.GoodService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/good")
public class GoodController
{
    private final GoodService goodService;

    @PostMapping
    public ResponseEntity<GoodResponseDto> createGood(
            @RequestBody GoodRequestDto requestDto
    )
    {
        return new ResponseEntity<>(
                goodService.saveGood(requestDto), HttpStatus.CREATED
        );
    }

    @GetMapping
    public ResponseEntity<Page<GoodResponseDto>> getAllGoods(Pageable pageable)
    {
        return new ResponseEntity<>(goodService.getAll(pageable), HttpStatus.OK);
    }
}