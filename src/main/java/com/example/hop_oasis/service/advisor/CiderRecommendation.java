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
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

@RequiredArgsConstructor
class CiderRecommendation implements Recommendation {
    private final BeerRepository beerRepository;
    private final CiderRepository ciderRepository;
    private final SnackRepository snackRepository;
    private final ProductBundleRepository productBundleRepository;
    private final Map<ItemType, Set<Long>> exclusionMap;

    @Override
    public Recommendations getRecommendations(Long productId) {
        final var cider = ciderRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Cider not found, id: %s", productId));

        // find bundles with the same cider
        final var bundlesWithTheSameCider =
                productBundleRepository.getBundlesWithSimilarName(cider.getCiderName(), PageRequest.of(0, 5));

        // some snacks
        final var randomSnacks = snackRepository.findRandomRecords(5);

        // other cider or beer
        final var randomCider = ciderRepository.findRandomRecords(2);
        randomCider.removeIf(c -> Objects.equals(c.getId(), productId));

        final var randomBeers = beerRepository.findRandomRecords(2);

        return new Recommendations(
                randomBeers,
                randomCider,
                randomSnacks,
                bundlesWithTheSameCider);
    }
}
