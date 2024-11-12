package com.example.hop_oasis.service.data;

import com.example.hop_oasis.handler.exception.ResourceNotFoundException;
import com.example.hop_oasis.model.*;
import com.example.hop_oasis.repository.*;
import com.example.hop_oasis.service.RecommendationsService;
import com.example.hop_oasis.service.advisor.Advisor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.util.stream.Collectors.*;

@Service
@RequiredArgsConstructor
public class RecommendationsServiceImpl implements RecommendationsService {
    private final CartRepository cartRepository;
    private final Advisor advisor;

    @Override
    public Recommendations getForCart(Long cartId) {
        final var cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found, id: %s", cartId));

        final var beerSet = new HashSet<Beer>();
        final var ciderSet = new HashSet<Cider>();
        final var snacksSet = new HashSet<Snack>();
        final var bundleSet = new HashSet<ProductBundle>();

        final var idMap = cart.getCartItems().stream()
                .filter(item -> item.getId() != null || item.getItemType() != null )
                .collect(groupingBy(CartItem::getItemType, mapping(CartItem::getItemId, toSet())));

        for (var entry : idMap.entrySet()) {
            final var recommendation = advisor.forProduct(entry.getKey(), idMap);

            for (var id: entry.getValue()) {
                final var recommendations = recommendation.getRecommendations(id);

                beerSet.addAll(recommendations.getBeers());
                ciderSet.addAll(recommendations.getCiders());
                snacksSet.addAll(recommendations.getSnacks());
                bundleSet.addAll(recommendations.getBundles());
            }
        }

        return new Recommendations(
                getFirstN(beerSet.stream().toList(), 5),
                getFirstN(ciderSet.stream().toList(), 5),
                getFirstN(snacksSet.stream().toList(), 5),
                getFirstN(bundleSet.stream().toList(), 5));
    }

    @Override
    public Recommendations getForProduct(Long productId, String itemTypeStr) {
        final var itemType = ItemType.valueOf(itemTypeStr);
        return advisor.forProduct(itemType, Map.of(itemType, Set.of(productId)))
                .getRecommendations(productId);
    }



    private <T> List<T> getFirstN(List<T> list, int n) {
        if (list == null || list.isEmpty() || n < 1) {
            return list;
        }

        return list.subList(0, Math.min(n, list.size()));
    }
}
