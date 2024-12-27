package com.example.hop_oasis.dto;

import jakarta.validation.Valid;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class CartUpdateRequestDto {
    private Long cartId;
    @Valid
    private List<ItemRequestDto> items;
}

