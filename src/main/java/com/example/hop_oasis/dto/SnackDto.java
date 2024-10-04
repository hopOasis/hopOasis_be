package com.example.hop_oasis.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public final class SnackDto {
    private Long id;
    private String snackName;
    private String description;
    private List<SnackImageDto> snackImageDto;
    private List<SnackOptionsDto> options;

}
