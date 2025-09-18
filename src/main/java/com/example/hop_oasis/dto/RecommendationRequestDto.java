package com.example.hop_oasis.dto;

import com.example.hop_oasis.model.ItemType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RecommendationRequestDto {
    @NotNull
    private Long itemId;
    @NotNull
    private ItemType itemType;
    @NotBlank
    @Size(max = 50)
    private String authorName;
    @NotBlank
    @Size(max = 50)
    private String teamRole;
    @NotBlank
    @Size(max = 500)
    private String textRecommendation;
}
