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
class ProductBundleRecommendation implements Recommendation {

    private final BeerRepository beerRepository;
    private final CiderRepository ciderRepository;
    private final SnackRepository snackRepository;
    private final ProductBundleRepository productBundleRepository;
    private final Map<ItemType, Set<Long>> exclusionMap;

    @Override
    public Recommendations getRecommendations(Long productId) {
        final var bundle = productBundleRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Bundle not found, id: %s", productId));

        // find bundles with the same name
        final var similarBundles =
                productBundleRepository.getBundlesWithSimilarName(bundle.getName(), PageRequest.of(0, 5));
        similarBundles.removeIf(b -> Objects.equals(b.getId(), productId));

        // find some beer
        final var randomBeers = beerRepository.findRandomRecords(5);

        // find some cider
        final var randomCider = ciderRepository.findRandomRecords(5);

        // find some snacks
        final var randomSnacks = snackRepository.findRandomRecords(5);

        return new Recommendations(
                randomBeers,
                randomCider,
                randomSnacks,
                similarBundles);
    }
}
