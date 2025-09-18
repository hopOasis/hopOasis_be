package com.example.hop_oasis.dto;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RecommendationResponseDto {
    private Long id;
    private Long itemId;
    private ItemShortInfoDto itemShortInfoDto;
    private String authorName;
    private String teamRole;
    private String textRecommendation;
    private String imageUrl;
    private String link;
    private LocalDateTime createdAt;
}
