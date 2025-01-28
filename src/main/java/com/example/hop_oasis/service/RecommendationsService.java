package com.example.hop_oasis.service;

import com.example.hop_oasis.model.ItemType;
import com.example.hop_oasis.service.advisor.ProposedProducts;

public interface RecommendationsService {
    ProposedProducts getForCart(Long cartId);
    ProposedProducts getForProduct(Long productId, ItemType itemType);
}
