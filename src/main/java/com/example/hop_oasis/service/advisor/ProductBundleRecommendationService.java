package com.example.hop_oasis.service.advisor;

import static com.example.hop_oasis.model.ItemType.BEER;
import static com.example.hop_oasis.model.ItemType.CIDER;
import static com.example.hop_oasis.model.ItemType.PRODUCT_BUNDLE;
import static com.example.hop_oasis.model.ItemType.SNACK;
import static com.example.hop_oasis.utils.GenericSpecification.getRandomRecords;
import static com.example.hop_oasis.utils.GenericSpecification.idsNotIn;
import static com.example.hop_oasis.utils.ProductBundleSpecification.bundlesWithNamesLike;
import static org.springframework.data.jpa.domain.Specification.allOf;

import com.example.hop_oasis.model.ItemType;
import com.example.hop_oasis.repository.BeerRepository;
import com.example.hop_oasis.repository.CiderRepository;
import com.example.hop_oasis.repository.ProductBundleRepository;
import com.example.hop_oasis.repository.SnackRepository;

import java.util.*;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@RequiredArgsConstructor
@Component
class ProductBundleRecommendationService implements RecommendationService {

    private final ProductBundleRepository bundleRepository;

    private final BeerRepository beerRepository;

    private final SnackRepository snackRepository;

    private final CiderRepository ciderRepository;

    @Override
    public ProposedProducts forProduct(Map<ItemType, Set<Long>> productsMap) {

        final var bundleIds = productsMap.get(PRODUCT_BUNDLE);
        var proposedProducts = new ProposedProducts();
        if (CollectionUtils.isEmpty(bundleIds)) {
            var randomBundles = bundleRepository.findAll(getRandomRecords(), PageRequest.of(0, 2)).getContent();
            proposedProducts.setBundles(randomBundles);

            return proposedProducts;
        }

        // get similar bundles
        final var bundleNames = bundleRepository.findNamesByIds(bundleIds);
        final var similarBundles = bundleRepository.findAll(
                        allOf(bundlesWithNamesLike(bundleNames), idsNotIn(bundleIds)), PageRequest.of(0, 2))
                .getContent();
        proposedProducts.setBundles(similarBundles);

        // and random beers ciders snacks
        final var randomBeers = beerRepository.findAll(allOf(idsNotIn(productsMap.get(BEER)), getRandomRecords()),
                PageRequest.of(0, 2)).getContent();
        proposedProducts.setBeers(randomBeers);

        final var randomCiders = ciderRepository.findAll(allOf(idsNotIn(productsMap.get(CIDER)), getRandomRecords()),
                PageRequest.of(0, 2)).getContent();
        proposedProducts.setCiders(randomCiders);

        final var randomSnacks = snackRepository.findAll(allOf(idsNotIn(productsMap.get(SNACK)), getRandomRecords()),
                PageRequest.of(0, 2)).getContent();
        proposedProducts.setSnacks(randomSnacks);

        return proposedProducts;
    }

    @Override
    public ItemType supportedItemType() {
        return PRODUCT_BUNDLE;
    }

}
