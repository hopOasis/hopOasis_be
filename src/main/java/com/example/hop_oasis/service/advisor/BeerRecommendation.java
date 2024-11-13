package com.example.hop_oasis.service.advisor;

import com.example.hop_oasis.model.Beer;
import com.example.hop_oasis.model.ItemType;
import com.example.hop_oasis.repository.BeerRepository;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.hop_oasis.utils.BeerSpecification.beerWithTheSameColors;
import static com.example.hop_oasis.utils.GenericSpecification.idsIn;
import static com.example.hop_oasis.utils.ProductBundleSpecification.bundlesWithNamesLike;

@RequiredArgsConstructor
class BeerRecommendation implements Recommendation {

    private final BeerRepository beerRepository;

    @Override
    public RecommendationPredicates getRecommendations(Map<ItemType, Set<Long>> productsMap) {

        final var beers = beerRepository.findAll(idsIn(productsMap.get(ItemType.BEER)));

        final var beerColors = beers.stream()
                .map(Beer::getBeerColor)
                .collect(Collectors.toSet());

        final var beerNames = beers.stream()
                .map(Beer::getBeerName)
                .collect(Collectors.toSet());

        return new RecommendationPredicates(
                beerWithTheSameColors(beerColors),
                null,
                null,
                bundlesWithNamesLike(beerNames));
    }
}
