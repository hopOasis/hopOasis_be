package com.example.hop_oasis.service.advisor;

import com.example.hop_oasis.model.Cider;
import com.example.hop_oasis.model.ItemType;
import com.example.hop_oasis.repository.CiderRepository;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.hop_oasis.utils.GenericSpecification.IdsIn;
import static com.example.hop_oasis.utils.GenericSpecification.IdsNotIn;
import static com.example.hop_oasis.utils.ProductBundleSpecification.pbWithNamesLike;

@RequiredArgsConstructor
class CiderRecommendation implements Recommendation {
    private final CiderRepository ciderRepository;

    @Override
    public RecommendationPredicates getRecommendations(Map<ItemType, Set<Long>> productsMap) {

        final var ciders = ciderRepository.findAll(IdsIn(productsMap.get(ItemType.CIDER)));
        final var ciderNames = ciders.stream().map(Cider::getCiderName).collect(Collectors.toSet());

        return new RecommendationPredicates(
                null,
                IdsNotIn(productsMap.get(ItemType.CIDER)),
                null,
                pbWithNamesLike(ciderNames));
    }
}
