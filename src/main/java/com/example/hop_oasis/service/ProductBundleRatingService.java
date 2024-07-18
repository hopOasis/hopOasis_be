package com.example.hop_oasis.service;

import com.example.hop_oasis.dto.ItemRatingDto;

public interface ProductBundleRatingService {
    double getAverageRating(Long productBundleId);

    int getRatingCount(Long productBundleId);

    ItemRatingDto getItemRating(Long productBundleId);
}
