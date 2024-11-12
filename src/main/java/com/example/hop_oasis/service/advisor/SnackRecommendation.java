package com.example.hop_oasis.service.advisor;

import com.example.hop_oasis.model.ItemType;
import com.example.hop_oasis.model.Snack;
import com.example.hop_oasis.repository.SnackRepository;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.hop_oasis.utils.GenericSpecification.IdsIn;
import static com.example.hop_oasis.utils.ProductBundleSpecification.pbWithNamesLike;

@RequiredArgsConstructor
class SnackRecommendation implements Recommendation {
    private final SnackRepository snackRepository;

    @Override
    public RecommendationPredicates getRecommendations(Map<ItemType, Set<Long>> productsMap) {

        final var snacks = snackRepository.findAll(IdsIn(productsMap.get(ItemType.SNACK)));
        final var snackNames = snacks.stream().map(Snack::getSnackName).collect(Collectors.toSet());

        return new RecommendationPredicates(
                null,
                null,
                null,
                pbWithNamesLike(snackNames));
    }
}
