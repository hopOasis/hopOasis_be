package com.example.hop_oasis.service.data;

import com.example.hop_oasis.handler.exception.ResourceNotFoundException;
import com.example.hop_oasis.model.*;
import com.example.hop_oasis.repository.*;
import com.example.hop_oasis.service.RecommendationsService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;

@Service
public class RecommendationsServiceImpl implements RecommendationsService {
    private final BeerRepository beerRepository;
    private final CiderRepository ciderRepository;
    private final SnackRepository snackRepository;
    private final ProductBundleRepository productBundleRepository;
    private final CartRepository cartRepository;

    public RecommendationsServiceImpl(BeerRepository beerRepository,
                                      CiderRepository ciderRepository,
                                      SnackRepository snackRepository,
                                      ProductBundleRepository productBundleRepository,
                                      CartRepository cartRepository
    ) {
        this.beerRepository = beerRepository;
        this.ciderRepository = ciderRepository;
        this.snackRepository = snackRepository;
        this.productBundleRepository = productBundleRepository;
        this.cartRepository = cartRepository;
    }

    @Override
    public Recommendations getForCart(Long cartId) {
        final var cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found, id: %s", cartId));

        final var beerSet = new HashSet<Beer>();
        final var ciderSet = new HashSet<Cider>();
        final var snacksSet = new HashSet<Snack>();
        final var bundleSet = new HashSet<ProductBundle>();

        for (CartItem cartItem : cart.getCartItems()) {
            final var recommendations = getForProduct(cartItem.getItemId(), cartItem.getItemType().name());
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
        return switch (ItemType.valueOf(itemType)) {
            case BEER -> getRecommendationsForBeer(productId);
            case CIDER -> getRecommendationsForCider(productId);
            case SNACK -> getRecommendationsForSnack(productId);
            case PRODUCT_BUNDLE -> getRecommendationsForBundle(productId);
        };

    }

    private Recommendations getRecommendationsForBeer(Long beerId) {
        final var beer = beerRepository.findById(beerId)
                .orElseThrow(() -> new ResourceNotFoundException("Beer not found, id: %s", beerId));

        // find bundles with the same beer
        final var bundlesWithTheSameBeer =
                getFirstN(productBundleRepository.getBundlesWithSimilarName(beer.getBeerName()), 5);

        // other beer with the same color or cider
        final var otherBeerWithSameColor = getFirstN(beerRepository.getOtherBeersWithTheSameColor(beer), 2);

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

    private Recommendations getRecommendationsForCider(Long ciderId) {
        final var cider = ciderRepository.findById(ciderId)
                .orElseThrow(() -> new ResourceNotFoundException("Cider not found, id: %s", ciderId));

        // find bundles with the same cider
        final var bundlesWithTheSameCider =
                getFirstN(productBundleRepository.getBundlesWithSimilarName(cider.getCiderName()), 5);

        // some snacks
        final var randomSnacks = snackRepository.findRandomRecords(5);

        // other cider or beer
        final var randomCider = ciderRepository.findRandomRecords(2);
        randomCider.removeIf(c -> Objects.equals(c.getId(), ciderId));

        final var randomBeers = beerRepository.findRandomRecords(2);

        return new Recommendations(
                randomBeers,
                randomCider,
                randomSnacks,
                bundlesWithTheSameCider);
    }

    private Recommendations getRecommendationsForSnack(Long snackId) {
        final var snack = snackRepository.findById(snackId)
                .orElseThrow(() -> new ResourceNotFoundException("Cider not found, id: %s", snackId));

        // find bundles with the same snacks
        final var bundlesWithTheSameSnacks =
                getFirstN(productBundleRepository.getBundlesWithSimilarName(snack.getSnackName()), 5);

        // find some beer
        final var randomBeers = beerRepository.findRandomRecords(2);

        // find random cider
        final var randomCider = ciderRepository.findRandomRecords(2);

        // find some snacks
        final var randomSnacks = snackRepository.findRandomRecords(5);
        randomSnacks.removeIf(c -> Objects.equals(c.getId(), snackId));

        return new Recommendations(
                randomBeers,
                randomCider,
                randomSnacks,
                bundlesWithTheSameSnacks);
    }

    private Recommendations getRecommendationsForBundle(Long bundleId) {
        final var bundle = productBundleRepository.findById(bundleId)
                .orElseThrow(() -> new ResourceNotFoundException("Bundle not found, id: %s", bundleId));

        // find bundles with the same name
        final var similarBundles = getFirstN(productBundleRepository.getBundlesWithSimilarName(bundle.getName()), 5);
        similarBundles.removeIf(b -> Objects.equals(b.getId(), bundleId));

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

    private <T> List<T> getFirstN(List<T> list, int n) {
        if (list == null || list.isEmpty() || n < 1) {
            return list;
        }

        return list.subList(0, Math.min(n, list.size()));
    }
}
