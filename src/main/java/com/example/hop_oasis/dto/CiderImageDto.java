package com.example.hop_oasis.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public final class CiderImageDto {
    private Long id;
    private byte[] image;
    private String name;
}
