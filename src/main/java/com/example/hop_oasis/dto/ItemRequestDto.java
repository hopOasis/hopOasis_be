
package com.example.hop_oasis.dto;

import com.example.hop_oasis.model.ItemType;
import jakarta.validation.constraints.Min;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public final class ItemRequestDto {
    private Long itemId;
    @Min(-1)
    private int quantity;
    private Double measureValue;
    private ItemType itemType;

}
