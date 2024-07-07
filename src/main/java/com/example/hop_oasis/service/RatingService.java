package com.example.hop_oasis.service;

import com.example.hop_oasis.model.ItemType;
import com.example.hop_oasis.dto.ItemRatingDto;

public interface RatingService {
    double getAverageRating(Long itemId, ItemType itemType);
    int getRatingCount(Long itemId, ItemType itemType);
    ItemRatingDto getItemRating(Long itemId, ItemType itemType);
}
