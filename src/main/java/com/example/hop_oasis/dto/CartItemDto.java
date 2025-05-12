package com.example.hop_oasis.dto;

import com.example.hop_oasis.model.ItemType;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public final class CartItemDto {
    private Long cartId;
    private Long itemId;
    private String itemTitle;
    private ItemType itemType;
    private double pricePerItem;
    private int quantity;
    private BigDecimal totalCost;
    private double measureValue;
    private List<String> imageName;
}
