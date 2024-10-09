
package com.example.hop_oasis.dto;

import com.example.hop_oasis.model.ItemType;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public final class ItemRequestDto {
    private Long itemId;
    private int quantity;
    private Double measureValue;
    private ItemType itemType;

}
