package com.example.hop_oasis.dto;

import com.example.hop_oasis.model.ItemType;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class RemoveItemRequestDto {
    private Long itemId;
    private ItemType itemType;
    private double measureValue;
}
