package com.example.hop_oasis.service;

import com.example.hop_oasis.dto.ItemRatingDto;

public interface BeerRatingService {
    double getAverageRating(Long beerId);

    int getRatingCount(Long beerId);

    ItemRatingDto getItemRating(Long beerId);
}
