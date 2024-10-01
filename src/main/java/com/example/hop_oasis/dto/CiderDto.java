package com.example.hop_oasis.dto;

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
    private List<CiderOptionsDto> options;
}
