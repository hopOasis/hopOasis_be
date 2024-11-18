package com.example.hop_oasis.service.advisor;

import com.example.hop_oasis.model.ItemType;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdvisorService {

    private final List<RecommendationService> recommendationServices;

    public ProposedProducts forProduct(ItemType type, Long productId) {
        var recommendation = getRecommendationService(type);
        return recommendation.forProduct(Map.of(type, Set.of(productId)));
    }

    public ProposedProducts forProducts(Map<ItemType, Set<Long>> productsMap) {
        var result = new ProposedProducts();
        for (var entry : productsMap.entrySet()) {
            var type = entry.getKey();
            var productIds = entry.getValue();
            var recommendation = getRecommendationService(type);
            var proposedProducts = recommendation.forProduct(Map.of(type, productIds));
            mergeProposedProducts(result, proposedProducts);
        }

         return result;
    }

    private void mergeProposedProducts(ProposedProducts proposedProducts, ProposedProducts newProposedProducts) {
        // TODO: use DTOs instead of entities.
        proposedProducts.getBeers().addAll(newProposedProducts.getBeers());
        proposedProducts.getCiders().addAll(newProposedProducts.getCiders());
        proposedProducts.getSnacks().addAll(newProposedProducts.getSnacks());
        proposedProducts.getBundles().addAll(newProposedProducts.getBundles());
    }

    private RecommendationService getRecommendationService(ItemType type) {
        return recommendationServices.stream()
            .filter(r -> type == r.supportedItemType())
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("No recommendation found for type: " + type));
    }

}
