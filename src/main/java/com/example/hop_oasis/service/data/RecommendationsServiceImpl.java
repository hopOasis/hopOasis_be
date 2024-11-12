package com.example.hop_oasis.service.data;

import com.example.hop_oasis.handler.exception.ResourceNotFoundException;
import com.example.hop_oasis.model.CartItem;
import com.example.hop_oasis.model.ItemType;
import com.example.hop_oasis.repository.*;
import com.example.hop_oasis.service.RecommendationsService;
import com.example.hop_oasis.service.advisor.Advisor;
import com.example.hop_oasis.service.advisor.RecommendationPredicates;
import com.example.hop_oasis.service.advisor.Recommendations;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;

import static com.example.hop_oasis.model.ItemType.*;
import static com.example.hop_oasis.utils.GenericSpecification.IdsNotIn;
import static com.example.hop_oasis.utils.GenericSpecification.getRandomRecords;
import static java.util.stream.Collectors.*;

@Service
@RequiredArgsConstructor
public class RecommendationsServiceImpl implements RecommendationsService {
    private final CartRepository cartRepository;
    private final Advisor advisor;
    private final BeerRepository beerRepository;
    private final CiderRepository ciderRepository;
    private final SnackRepository snackRepository;
    private final ProductBundleRepository productBundleRepository;

    @Override
    public Recommendations getForCart(Long cartId) {
        final var cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found, id: %s", cartId));


        final var idMap = cart.getCartItems().stream()
                .filter(item -> item.getId() != null || item.getItemType() != null)
                .collect(groupingBy(CartItem::getItemType, mapping(CartItem::getItemId, toSet())));

        final var predicates = new ArrayList<RecommendationPredicates>();

        for (var entry : idMap.entrySet()) {
            predicates.add(
                    advisor.forProduct(entry.getKey()).getRecommendations(idMap));
        }

        return buildRecommendations(new RecommendationPredicates(
                combineFor(predicates, RecommendationPredicates::beerSpecs),
                combineFor(predicates, RecommendationPredicates::ciderSpecs),
                combineFor(predicates, RecommendationPredicates::snackSpecs),
                combineFor(predicates, RecommendationPredicates::pbSpecs)
        ), idMap);
    }

    @Override
    public Recommendations getForProduct(Long productId, String itemTypeStr) {

        final var itemType = valueOf(itemTypeStr);
        final var itemMap = Map.of(valueOf(itemTypeStr), Set.of(productId));

        checkExistence(itemType, productId);

        final var predicates = advisor.forProduct(itemType)
                .getRecommendations(itemMap);

        return buildRecommendations(predicates, itemMap);
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

    private Recommendations buildRecommendations(RecommendationPredicates predicates,
                                                 Map<ItemType, Set<Long>> itemMap) {
        final var beerPredicates = Specification.allOf(IdsNotIn(itemMap.get(BEER)),
                predicates.beerSpecs() == null
                        ? getRandomRecords()
                        : predicates.beerSpecs());

        final var ciderPredicates = Specification.allOf(IdsNotIn(itemMap.get(CIDER)),
                predicates.ciderSpecs() == null
                        ? getRandomRecords()
                        : predicates.ciderSpecs());

        final var snackPredicates = Specification.allOf(IdsNotIn(itemMap.get(SNACK)),
                predicates.snackSpecs() == null
                        ? getRandomRecords()
                        : predicates.snackSpecs());

        final var pbPredicates = Specification.allOf(IdsNotIn(itemMap.get(PRODUCT_BUNDLE)),
                predicates.pbSpecs() == null
                        ? getRandomRecords()
                        : predicates.pbSpecs());

        return new Recommendations(
                beerRepository.findAll(beerPredicates, PageRequest.of(0, 5)).getContent(),
                ciderRepository.findAll(ciderPredicates, PageRequest.of(0, 5)).getContent(),
                snackRepository.findAll(snackPredicates, PageRequest.of(0, 5)).getContent(),
                productBundleRepository.findAll(pbPredicates, PageRequest.of(0, 5)).getContent());
    }

    private <T> Specification<T> combineFor(List<RecommendationPredicates> predicates,
                                            Function<RecommendationPredicates, Specification<T>> function) {

        return predicates == null || predicates.isEmpty()
                ? null
                : Specification.anyOf(predicates.stream()
                .filter(Objects::nonNull)
                .map(function)
                .toList());
    }
}
