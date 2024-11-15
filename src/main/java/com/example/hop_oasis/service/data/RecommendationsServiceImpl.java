package com.example.hop_oasis.service.data;

import com.example.hop_oasis.handler.exception.ResourceNotFoundException;
import com.example.hop_oasis.model.CartItem;
import com.example.hop_oasis.model.ItemType;
import com.example.hop_oasis.repository.*;
import com.example.hop_oasis.service.RecommendationsService;
import com.example.hop_oasis.service.advisor.AdvisorService;
import com.example.hop_oasis.service.advisor.ProposedProducts;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;

import static com.example.hop_oasis.model.ItemType.*;
import static com.example.hop_oasis.utils.GenericSpecification.idsNotIn;
import static com.example.hop_oasis.utils.GenericSpecification.getRandomRecords;
import static java.util.stream.Collectors.*;

@Service
@RequiredArgsConstructor
public class RecommendationsServiceImpl implements RecommendationsService {

    private final CartRepository cartRepository;

    private final AdvisorService advisorService;

    private final BeerRepository beerRepository;

    private final CiderRepository ciderRepository;

    private final SnackRepository snackRepository;

    private final ProductBundleRepository productBundleRepository;

    @Override
    public ProposedProducts getForCart(Long cartId) {
        final var cart = cartRepository.findById(cartId)
            .orElseThrow(() -> new ResourceNotFoundException("Cart not found, id: %s", cartId));

        final var idMap = cart.getCartItems().stream()
            .filter(item -> item.getId() != null || item.getItemType() != null)
            .collect(groupingBy(CartItem::getItemType, mapping(CartItem::getItemId, toSet())));

        final var predicates = idMap.keySet().stream()
            .map(longs -> advisorService.forProduct(longs).forProduct(idMap))
            .toList();

        return fetchRecommendations(new RecommendationPredicates(
            combineFor(predicates, RecommendationPredicates::beerSpecs),
            combineFor(predicates, RecommendationPredicates::ciderSpecs),
            combineFor(predicates, RecommendationPredicates::snackSpecs),
            combineFor(predicates, RecommendationPredicates::pbSpecs)
        ), idMap);
    }

    @Override
    public ProposedProducts getForProduct(Long productId, ItemType itemType) {
        final var itemMap = Map.of(itemType, Set.of(productId));
        checkExistence(itemType, productId);
        final var predicates = advisorService.forProduct(itemType)
            .forProduct(itemMap);

        return fetchRecommendations(predicates, itemMap);
    }

    private void checkExistence(ItemType itemType, Long productId) {
        final var repository = switch (itemType) {
            case BEER -> beerRepository;
            case CIDER -> ciderRepository;
            case SNACK -> snackRepository;
            case PRODUCT_BUNDLE -> productBundleRepository;
        };

        repository.findById(productId)
            .orElseThrow(
                () -> new ResourceNotFoundException("%s with id: %s not found".formatted(itemType, productId)));
    }

    private ProposedProducts fetchRecommendations(RecommendationPredicates predicates, Map<ItemType, Set<Long>> itemMap) {
        final var beerPredicates = Specification.allOf(idsNotIn(itemMap.get(BEER)),
            predicates.beerSpecs() == null
                ? getRandomRecords()
                : predicates.beerSpecs());

        final var ciderPredicates = Specification.allOf(idsNotIn(itemMap.get(CIDER)),
            predicates.ciderSpecs() == null
                ? getRandomRecords()
                : predicates.ciderSpecs());

        final var snackPredicates = Specification.allOf(idsNotIn(itemMap.get(SNACK)),
            predicates.snackSpecs() == null
                ? getRandomRecords()
                : predicates.snackSpecs());

        final var pbPredicates = Specification.allOf(idsNotIn(itemMap.get(PRODUCT_BUNDLE)),
            predicates.pbSpecs() == null
                ? getRandomRecords()
                : predicates.pbSpecs());

        var limit = PageRequest.of(0, 5);
        return new ProposedProducts(
            beerRepository.findAll(beerPredicates, limit).getContent(),
            ciderRepository.findAll(ciderPredicates, limit).getContent(),
            snackRepository.findAll(snackPredicates, limit).getContent(),
            productBundleRepository.findAll(pbPredicates, limit).getContent());
    }

    private <T> Specification<T> combineFor(List<RecommendationPredicates> predicates, Function<RecommendationPredicates, Specification<T>> function) {

        return predicates == null || predicates.isEmpty()
            ? null
            : Specification.anyOf(predicates.stream()
                .filter(Objects::nonNull)
                .map(function)
                .toList());
    }

}
