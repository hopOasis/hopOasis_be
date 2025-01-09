package com.example.hop_oasis.dto;

import jakarta.validation.Valid;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public final class CiderDto {
    private Long id;
    private String ciderName;
    private String description;
    private List<CiderImageDto> image;
    @Valid
    private List<CiderOptionsDto> options;
}
