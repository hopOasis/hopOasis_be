package com.example.hop_oasis.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class CartUpdateRequestDto {
    private Long cartId;
    private List<ItemRequestDtoWithMeasure> items;
}

