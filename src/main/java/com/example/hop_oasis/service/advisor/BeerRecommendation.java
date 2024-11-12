package com.example.hop_oasis.service.advisor;

import com.example.hop_oasis.handler.exception.ResourceNotFoundException;
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

@RequiredArgsConstructor
class BeerRecommendation implements Recommendation {

    private final BeerRepository beerRepository;
    private final CiderRepository ciderRepository;
    private final SnackRepository snackRepository;
    private final ProductBundleRepository productBundleRepository;
    private final Map<ItemType, Set<Long>> exclusionMap;

    @Override
    public Recommendations getRecommendations(Long productId) {
        final var beer = beerRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Beer not found, id: %s", productId));

        // find bundles with the same beer
        final var bundlesWithTheSameBeer =
                productBundleRepository.getBundlesWithSimilarName(beer.getBeerName(), PageRequest.of(0, 5));

        // other beer with the same color or cider
        final var otherBeerWithSameColor = beerRepository.getOtherBeersWithTheSameColor(beer, PageRequest.of(0, 2));

        // random cider
        final var randomCider = ciderRepository.findRandomRecords(2);

        // some snacks
        final var randomSnacks = snackRepository.findRandomRecords(5);

        return new Recommendations(
                otherBeerWithSameColor,
                randomCider,
                randomSnacks,
                bundlesWithTheSameBeer);
    }
}
