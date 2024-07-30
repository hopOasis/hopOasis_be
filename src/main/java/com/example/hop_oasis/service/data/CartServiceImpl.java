package com.example.hop_oasis.service.data;

import com.example.hop_oasis.dto.*;
import com.example.hop_oasis.model.*;
import com.example.hop_oasis.repository.CartRepository;
import com.example.hop_oasis.hendler.exception.ResourceNotFoundException;
import com.example.hop_oasis.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import static com.example.hop_oasis.hendler.exception.message.ExceptionMessage.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final BeerService beerService;
    private final SnackService snackService;
    private final ProductBundleService bundleService;
    private final CiderService ciderService;

    @Override
    public CartDto getAllItems() {
        List<CartItemDto> items = new ArrayList<>();

        addItemsToCart(items, ItemType.BEER);
        addItemsToCart(items, ItemType.SNACK);
        addItemsToCart(items, ItemType.PRODUCT_BUNDLE);
        addItemsToCart(items, ItemType.CIDER);

        CartDto result = new CartDto();
        result.setItems(items);
        result.setPriceForAll(items.stream()
                .map(CartItemDto::getTotalCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        log.debug("Return cart with all items: {}", result);
        return result;
    }

    private void addItemsToCart(List<CartItemDto> items, ItemType itemType) {
        List<Cart> carts = cartRepository.findByItemType(itemType);
        for (Cart cart : carts) {
            CartItemDto dto = new CartItemDto();
            dto.setItemId(cart.getItemId());
            dto.setQuantity(cart.getQuantity());

            switch (itemType) {
                case BEER:
                    BeerInfoDto beerInfo = beerService.getBeerById(cart.getItemId());
                    if (beerInfo != null) {
                        dto.setItemTitle(beerInfo.getBeerName());
                        dto.setPricePerItem(determinePrice(beerInfo.getPriceLarge(), beerInfo.getPriceSmall()));
                    }
                    break;
                case SNACK:
                    SnackInfoDto snackInfo = snackService.getSnackById(cart.getItemId());
                    if (snackInfo != null) {
                        dto.setItemTitle(snackInfo.getSnackName());
                        dto.setPricePerItem(determinePrice(snackInfo.getPriceLarge(), snackInfo.getPriceSmall()));
                    }
                    break;
                case PRODUCT_BUNDLE:
                    ProductBundleInfoDto bundleInfo = bundleService.getProductBundleById(cart.getItemId());
                    if (bundleInfo != null) {
                        dto.setItemTitle(bundleInfo.getName());
                        dto.setPricePerItem(bundleInfo.getPrice());
                    }
                    break;
                case CIDER:
                    CiderInfoDto ciderInfo = ciderService.getCiderById(cart.getItemId());
                    if (ciderInfo != null) {
                        dto.setItemTitle(ciderInfo.getCiderName());
                        dto.setPricePerItem(determinePrice(ciderInfo.getPriceLarge(), ciderInfo.getPriceSmall()));
                    }
                    break;
            }
            BigDecimal roundedTotalCost = BigDecimal.valueOf(dto.getQuantity() * dto.getPricePerItem())
                    .setScale(2, RoundingMode.HALF_UP);
            dto.setTotalCost(roundedTotalCost);
            items.add(dto);
        }
    }

    @Override
    public CartItemDto add(Long itemId, int quantity, ItemType itemType) {
        Cart cart = cartRepository.findByItemIdAndItemType(itemId, itemType);
        if (cart == null) {
            cart = new Cart();
            cart.setItemId(itemId);
            cart.setItemType(itemType);
            cart.setQuantity(quantity);
        } else {
            cart.setQuantity(cart.getQuantity() + quantity);
        }
        cartRepository.save(cart);
        return createCartItemDto(cart);
    }

    @Override
    public CartItemDto updateQuantity(Long itemId, int quantity, ItemType itemType) {
        Cart cart = cartRepository.findByItemIdAndItemType(itemId, itemType);
        if (cart != null) {
            cart.setQuantity(quantity);
            cartRepository.save(cart);
            return createCartItemDto(cart);
        }
        throw new ResourceNotFoundException(RESOURCE_NOT_FOUND, "");
    }

    @Override
    public void removeItem(Long itemId, ItemType itemType) {
        Cart cart = cartRepository.findByItemIdAndItemType(itemId, itemType);
        if (cart != null) {
            cartRepository.delete(cart);
        } else {
            throw new ResourceNotFoundException(RESOURCE_NOT_FOUND, "");
        }
    }

    @Override
    public void delete() {
        log.debug("Clear cart");
        cartRepository.deleteAll();
    }

    private CartItemDto createCartItemDto(Cart cart) {
        CartItemDto dto = new CartItemDto();
        dto.setItemId(cart.getItemId());
        dto.setQuantity(cart.getQuantity());

        switch (cart.getItemType()) {
            case BEER:
                BeerInfoDto beerInfo = beerService.getBeerById(cart.getItemId());
                if (beerInfo != null) {
                    dto.setItemTitle(beerInfo.getBeerName());
                    dto.setPricePerItem(determinePrice(beerInfo.getPriceLarge(), beerInfo.getPriceSmall()));
                }
                break;
            case SNACK:
                SnackInfoDto snackInfo = snackService.getSnackById(cart.getItemId());
                if (snackInfo != null) {
                    dto.setItemTitle(snackInfo.getSnackName());
                    dto.setPricePerItem(determinePrice(snackInfo.getPriceLarge(), snackInfo.getPriceSmall()));
                }
                break;
            case PRODUCT_BUNDLE:
                ProductBundleInfoDto bundleInfo = bundleService.getProductBundleById(cart.getItemId());
                if (bundleInfo != null) {
                    dto.setItemTitle(bundleInfo.getName());
                    dto.setPricePerItem(bundleInfo.getPrice());
                }
                break;
            case CIDER:
                CiderInfoDto ciderInfo = ciderService.getCiderById(cart.getItemId());
                if (ciderInfo != null) {
                    dto.setItemTitle(ciderInfo.getCiderName());
                    dto.setPricePerItem(determinePrice(ciderInfo.getPriceLarge(), ciderInfo.getPriceSmall()));
                }
                break;
        }
        BigDecimal roundedTotalCost = BigDecimal.valueOf(dto.getQuantity() * dto.getPricePerItem())
                .setScale(2, RoundingMode.HALF_UP);
        dto.setTotalCost(roundedTotalCost);
        return dto;
    }

    private double determinePrice(double priceLarge, double priceSmall) {
        return priceLarge != 0 ? priceLarge : priceSmall;
    }
}
