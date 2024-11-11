package com.example.hop_oasis.service.data;

import com.example.hop_oasis.handler.exception.ResourceNotFoundException;
import com.example.hop_oasis.model.*;
import com.example.hop_oasis.repository.*;
import com.example.hop_oasis.service.RecommendationsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RecommendationsServiceImpl implements RecommendationsService {
    private final BeerRepository beerRepository;
    private final CiderRepository ciderRepository;
    private final SnackRepository snackRepository;
    private final ProductBundleRepository productBundleRepository;
    private final CartRepository cartRepository;

    @Override
    public Recommendations getForCart(Long cartId) {
        final var cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found, id: %s", cartId));

        final var beerSet = new HashSet<Beer>();
        final var ciderSet = new HashSet<Cider>();
        final var snacksSet = new HashSet<Snack>();
        final var bundleSet = new HashSet<ProductBundle>();

        for (CartItem cartItem : cart.getCartItems()) {
            final var recommendations = getRecommendation(cartItem.getItemType())
                    .getRecommendations(cartItem.getItemId());

            beerSet.addAll(recommendations.getBeers());
            ciderSet.addAll(recommendations.getCiders());
            snacksSet.addAll(recommendations.getSnacks());
            bundleSet.addAll(recommendations.getBundles());
        }

        // remove what already in cart
        for (CartItem cartItem : cart.getCartItems()) {
            switch (cartItem.getItemType()) {
                case BEER -> beerSet.removeIf(beer -> beer.getId().equals(cartItem.getItemId()));
                case CIDER -> ciderSet.removeIf(cider -> cider.getId().equals(cartItem.getItemId()));
                case SNACK -> snacksSet.removeIf(snack -> snack.getId().equals(cartItem.getItemId()));
                case PRODUCT_BUNDLE -> bundleSet.removeIf(pb -> pb.getId().equals(cartItem.getItemId()));
                case null -> throw new IllegalStateException("Item type is null");
            }
        }

        return new Recommendations(
                getFirstN(beerSet.stream().toList(), 5),
                getFirstN(ciderSet.stream().toList(), 5),
                getFirstN(snacksSet.stream().toList(), 5),
                getFirstN(bundleSet.stream().toList(), 5));
    }

    @Override
    public Recommendations getForProduct(Long productId, String itemType) {
        return getRecommendation(ItemType.valueOf(itemType)).getRecommendations(productId);
    }

    private Recommendation getRecommendation(ItemType itemType) {
        return switch (itemType) {
            case BEER -> new BeerRecommendation();
            case CIDER -> new CiderRecommendation();
            case SNACK -> new SnackRecommendation();
            case PRODUCT_BUNDLE -> new BundleRecommendation();
        };
    }

    private interface Recommendation {
        Recommendations getRecommendations(Long productId);
    }

    private class BeerRecommendation implements Recommendation {

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

    private class CiderRecommendation implements Recommendation {
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

    private class SnackRecommendation implements Recommendation {
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

    private class BundleRecommendation implements Recommendation {
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

    private <T> List<T> getFirstN(List<T> list, int n) {
        if (list == null || list.isEmpty() || n < 1) {
            return list;
        }

        return list.subList(0, Math.min(n, list.size()));
    }
}
