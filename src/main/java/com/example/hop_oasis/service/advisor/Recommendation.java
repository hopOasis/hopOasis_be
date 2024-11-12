package com.example.hop_oasis.service.advisor;

import com.example.hop_oasis.model.ItemType;

import java.util.Map;
import java.util.Set;

public interface Recommendation {
    RecommendationPredicates getRecommendations(Map<ItemType, Set<Long>> productsMap);
}
