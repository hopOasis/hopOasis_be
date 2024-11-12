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

import static com.example.hop_oasis.utils.GenericSpecification.IdsNotIn;
import static com.example.hop_oasis.utils.GenericSpecification.getRandomRecords;
import static com.example.hop_oasis.utils.ProductBundleSpecification.pbWithNameLike;
import static org.springframework.data.jpa.domain.Specification.allOf;

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

        final var randomBeers = beerRepository.findAll(
                allOf(IdsNotIn(exclusionMap.get(ItemType.BEER)), getRandomRecords()),
                PageRequest.of(0, 2)).getContent();

        final var randomCider = ciderRepository.findAll(
                allOf(IdsNotIn(exclusionMap.get(ItemType.CIDER)), getRandomRecords()),
                PageRequest.of(0, 2)).getContent();

        final var randomSnacks = snackRepository.findAll(
                allOf(IdsNotIn(exclusionMap.get(ItemType.SNACK)), getRandomRecords()),
                PageRequest.of(0, 5)).getContent();

        final var bundlesWithTheSameSnacks = productBundleRepository.findAll(pbWithNameLike(snack.getSnackName())
                .and(IdsNotIn(exclusionMap.get(ItemType.PRODUCT_BUNDLE))), PageRequest.of(0, 5)).getContent();

        return new Recommendations(
                randomBeers,
                randomCider,
                randomSnacks,
                bundlesWithTheSameSnacks);
    }
}
