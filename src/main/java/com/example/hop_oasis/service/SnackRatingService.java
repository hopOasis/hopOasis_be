package com.example.hop_oasis.service;

import com.example.hop_oasis.dto.ItemRatingDto;

public interface SnackRatingService {

  double getAverageRating(Long snackId);

  int getRatingCount(Long snackId);

  ItemRatingDto getItemRating(Long snackId);
}
