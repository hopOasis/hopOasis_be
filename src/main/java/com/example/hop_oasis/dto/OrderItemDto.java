package com.example.hop_oasis.dto;

import com.example.hop_oasis.model.ItemType;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderItemDto {
    private Long id;
    private String itemTitle;
    private ItemType itemType;
    private Integer quantity;
    private double price;

}
