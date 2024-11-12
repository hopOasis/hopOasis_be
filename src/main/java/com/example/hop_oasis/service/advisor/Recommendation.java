package com.example.hop_oasis.service.advisor;

import com.example.hop_oasis.model.Recommendations;

import java.util.Set;

public interface Recommendation {
    Recommendations getRecommendations(Long productId);
}
