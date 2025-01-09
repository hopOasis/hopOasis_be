package com.example.hop_oasis.dto;

import com.example.hop_oasis.model.ItemType;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ReviewInfoDto {
    private Long id;
    private Long userId;
    private String firstName;
    private String lastName;
    private Long itemId;
    private ItemType itemType;
    private LocalDateTime createdAt;
    private String content;
}
