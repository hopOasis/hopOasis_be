package com.example.hop_oasis.dto;
import com.example.hop_oasis.model.ItemType;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ItemRequestDto {
    private Long itemId;
    private int quantity;
    private ItemType itemType;
}