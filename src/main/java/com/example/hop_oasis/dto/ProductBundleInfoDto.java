package com.example.hop_oasis.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public final class ProductBundleInfoDto {
    private Long id;
    private String name;
    private double price;
    private String description;
    private List<String> productImageName;
}
