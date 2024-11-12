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
import java.util.Objects;
import java.util.Set;

@RequiredArgsConstructor
class SnackRecommendation implements Recommendation {
    private final BeerRepository beerRepository;
    private final CiderRepository ciderRepository;
    private final SnackRepository snackRepository;
    private final ProductBundleRepository productBundleRepository;
    private final Map<ItemType, Set<Long>> exclusionMap;

    @Override
    public Recommendations getRecommendations(Long productId) {
        final var snack = snackRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Cider not found, id: %s", productId));

        // find bundles with the same snacks
        final var bundlesWithTheSameSnacks =
                productBundleRepository.getBundlesWithSimilarName(snack.getSnackName(), PageRequest.of(0, 5));

        // find some beer
        final var randomBeers = beerRepository.findRandomRecords(2);

        // find random cider
        final var randomCider = ciderRepository.findRandomRecords(2);

        // find some snacks
        final var randomSnacks = snackRepository.findRandomRecords(5);
        randomSnacks.removeIf(c -> Objects.equals(c.getId(), productId));

        return new Recommendations(
                randomBeers,
                randomCider,
                randomSnacks,
                bundlesWithTheSameSnacks);
    }
}
