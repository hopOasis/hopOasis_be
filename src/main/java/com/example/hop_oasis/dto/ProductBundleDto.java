package com.example.hop_oasis.dto;

import jakarta.validation.Valid;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public final class ProductBundleDto {
    private Long id;
    private String name;
    private String description;
    private List<ProductBundleImageDto> imageDto;
    @Valid
    private List<ProductBundleOptionsDto> options;

}
