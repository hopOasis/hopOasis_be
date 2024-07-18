package com.example.hop_oasis.service;

import com.example.hop_oasis.dto.ItemRatingDto;

public interface CiderRatingService {
    double getAverageRating(Long ciderId);

    int getRatingCount(Long ciderId);

    ItemRatingDto getItemRating(Long ciderId);
}
