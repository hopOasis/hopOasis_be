package com.example.hop_oasis.dto;

import com.example.hop_oasis.enums.Reaction;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ReactionDto {
    private Reaction reaction;
}
