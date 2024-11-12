package com.example.hop_oasis.service;

import com.example.hop_oasis.service.advisor.Recommendations;

public interface RecommendationsService {
    Recommendations getForCart(Long cartId);
    Recommendations getForProduct(Long productId, String itemType);
}
