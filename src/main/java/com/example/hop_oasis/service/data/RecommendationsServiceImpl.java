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

        return advisorService.forProducts(idMap);
    }

    @Override
    public ProposedProducts getForProduct(Long productId, ItemType itemType) {
        final var itemMap = Map.of(itemType, Set.of(productId));
        checkExistence(itemType, productId);
        return advisorService.forProduct(itemType, productId);
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

}
