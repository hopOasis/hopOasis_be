package com.example.hop_oasis.dto;

import com.example.hop_oasis.model.ItemType;
import com.example.hop_oasis.model.MeasureValue;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public final class ItemRequestDto {
    private Long itemId;
    private int quantity;
    private MeasureValue measureValue;
    private ItemType itemType;

}