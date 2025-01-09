package com.example.hop_oasis.dto;

import com.example.hop_oasis.model.ItemType;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ReviewDto {
    private Long id;
    private Long userId;
    private Long itemId;
    private ItemType itemType;
    private String content;
}
