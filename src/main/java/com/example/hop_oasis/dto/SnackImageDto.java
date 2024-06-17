package com.example.hop_oasis.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class SnackImageDto {
    private Long id;
    private byte[] image;
    private String name;
}
