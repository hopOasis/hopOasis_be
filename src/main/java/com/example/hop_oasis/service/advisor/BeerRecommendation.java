package com.example.hop_oasis.service.advisor;

import com.example.hop_oasis.model.Beer;
import com.example.hop_oasis.model.ItemType;
import com.example.hop_oasis.model.Recommendations;
import com.example.hop_oasis.repository.BeerRepository;
import com.example.hop_oasis.repository.CiderRepository;
import com.example.hop_oasis.repository.ProductBundleRepository;
import com.example.hop_oasis.repository.SnackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.hop_oasis.utils.BeerSpecification.beerWithTheSameColors;
import static com.example.hop_oasis.utils.GenericSpecification.*;
import static com.example.hop_oasis.utils.ProductBundleSpecification.pbWithNamesLike;
import static org.springframework.data.jpa.domain.Specification.allOf;

@RequiredArgsConstructor
class BeerRecommendation implements Recommendation {

    private final BeerRepository beerRepository;
    private final CiderRepository ciderRepository;
    private final SnackRepository snackRepository;
    private final ProductBundleRepository productBundleRepository;

    @Override
    public Recommendations getRecommendations(Map<ItemType, Set<Long>> productsMap) {

        final var beers = beerRepository.findAll(IdsIn(productsMap.get(ItemType.BEER)));

        final var beerColors = beers.stream()
                .map(Beer::getBeerColor)
                .collect(Collectors.toSet());

        final var beerNames = beers.stream()
                .map(Beer::getBeerName)
                .collect(Collectors.toSet());

        final var otherBeerWithSameColor = beerRepository.findAll(
                beerWithTheSameColors(beerColors)
                        .and(IdsNotIn(productsMap.get(ItemType.BEER))), PageRequest.of(0, 2)).getContent();

        final var randomCider = ciderRepository.findAll(
                allOf(IdsNotIn(productsMap.get(ItemType.CIDER)), getRandomRecords()),
                PageRequest.of(0, 2)).getContent();

        final var randomSnacks = snackRepository.findAll(
                allOf(IdsNotIn(productsMap.get(ItemType.SNACK)), getRandomRecords()),
                PageRequest.of(0, 5)).getContent();

        final var bundlesWithTheSameBeer = productBundleRepository.findAll(
                pbWithNamesLike(beerNames)
                        .and(IdsNotIn(productsMap.get(ItemType.PRODUCT_BUNDLE))),
                PageRequest.of(0, 5)).getContent();

        return new Recommendations(
                otherBeerWithSameColor,
                randomCider,
                randomSnacks,
                bundlesWithTheSameBeer);
    }
}
