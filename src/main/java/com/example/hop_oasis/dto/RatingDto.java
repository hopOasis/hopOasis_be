package com.example.hop_oasis.dto;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RatingDto {
    @Min(1)
    @Max(5)
    private double ratingValue;
}
