package com.example.hop_oasis.service.advisor;

import com.example.hop_oasis.model.ItemType;
import com.example.hop_oasis.model.ProductBundle;
import com.example.hop_oasis.repository.ProductBundleRepository;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.hop_oasis.utils.GenericSpecification.idsIn;
import static com.example.hop_oasis.utils.ProductBundleSpecification.bundlesWithNamesLike;

@RequiredArgsConstructor
class ProductBundleRecommendation implements Recommendation {

    private final ProductBundleRepository productBundleRepository;

    @Override
    public RecommendationPredicates getRecommendations(Map<ItemType, Set<Long>> productsMap) {

        final var bundles = productBundleRepository.findAll(idsIn(productsMap.get(ItemType.PRODUCT_BUNDLE)));
        final var bundleNames = bundles.stream().map(ProductBundle::getName).collect(Collectors.toSet());

        return new RecommendationPredicates(
                null,
                null,
                null,
                bundlesWithNamesLike(bundleNames));
    }
}
