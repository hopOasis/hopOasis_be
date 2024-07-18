package com.example.hop_oasis.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ProductBundleInfoDto {
    private Long id;
    private String name;
    private double price;
    private String description;
    private List<String> productImageName;
    private double averageRating;
    private int ratingCount;
}
