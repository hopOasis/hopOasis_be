package com.example.hop_oasis.service.data;

import com.example.hop_oasis.component.Cart;
import com.example.hop_oasis.dto.*;
import com.example.hop_oasis.model.ItemType;
import com.example.hop_oasis.service.CartService;
import com.example.hop_oasis.hendler.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.function.Function;

import static com.example.hop_oasis.hendler.exception.message.ExceptionMessage.*;

@Service
@RequiredArgsConstructor
@Log4j2
public class CartServiceImpl implements CartService {
    private final Cart cart;
    private final BeerServiceImpl beerService;
    private final ProductBundleServiceImpl bundleService;
    private final SnackServiceImpl snackService;
    private final CiderServiceImpl ciderService;

    @Override
    public CartDto getAllItems() {
        List<CartItemDto> items = new ArrayList<>();

        addItemsToCart(items, cart.getBeers(), beerService::getBeerById, ItemType.BEER);
        addItemsToCart(items, cart.getSnacks(), snackService::getSnackById, ItemType.SNACK);
        addItemsToCart(items, cart.getProductBundles(), bundleService::getProductBundleById, ItemType.PRODUCT_BUNDLE);
        addItemsToCart(items, cart.getCider(), ciderService::getCiderById, ItemType.CIDER);

        CartDto result = new CartDto();
        result.setItems(items);
        result.setPriceForAll(items.stream()
                .map(CartItemDto::getTotalCost)
                .reduce(0.0, Double::sum));
        log.debug("Return cart with all items: {}", result);
        return result;
    }

    private <T> void addItemsToCart(List<CartItemDto> items, Map<Long, Integer> cartItems, Function<Long, T> fetchFunction, ItemType itemType) {
        for (Map.Entry<Long, Integer> entry : cartItems.entrySet()) {
            Long itemId = entry.getKey();
            int quantity = entry.getValue();
            T itemInfoDto = fetchFunction.apply(itemId);
            if (itemInfoDto == null) {
                log.error("{} with ID {} not found", itemType, itemId);
                throw new ResourceNotFoundException(RESOURCE_NOT_FOUND, "");
            }
            items.add(createCartItemDto(itemInfoDto, quantity, itemType));
        }
    }

    private <T> CartItemDto createCartItemDto(T itemInfoDto, int quantity, ItemType itemType) {
        CartItemDto cartItemDto = new CartItemDto();
        double pricePerItem;

        switch (itemType) {
            case BEER:
                BeerInfoDto beerInfoDto = (BeerInfoDto) itemInfoDto;
                cartItemDto.setItemTitle(beerInfoDto.getBeerName());
                pricePerItem = determinePrice(beerInfoDto.getPriceLarge(), beerInfoDto.getPriceSmall());
                break;
            case SNACK:
                SnackInfoDto snackInfoDto = (SnackInfoDto) itemInfoDto;
                cartItemDto.setItemTitle(snackInfoDto.getSnackName());
                pricePerItem = determinePrice(snackInfoDto.getPriceLarge(), snackInfoDto.getPriceSmall());
                break;
            case PRODUCT_BUNDLE:
                ProductBundleInfoDto bundleInfoDto = (ProductBundleInfoDto) itemInfoDto;
                cartItemDto.setItemTitle(bundleInfoDto.getName());
                pricePerItem = bundleInfoDto.getPrice();
                break;
            case CIDER:
                CiderInfoDto ciderInfoDto = (CiderInfoDto) itemInfoDto;
                cartItemDto.setItemTitle(ciderInfoDto.getCiderName());
                pricePerItem = determinePrice(ciderInfoDto.getPriceLarge(), ciderInfoDto.getPriceSmall());
                break;
            default:
                throw new IllegalArgumentException("Unsupported item type: " + itemType);
        }

        cartItemDto.setPricePerItem(pricePerItem);
        cartItemDto.setQuantity(quantity);
        cartItemDto.setTotalCost(quantity * pricePerItem);
        return cartItemDto;
    }

    private double determinePrice(double priceLarge, double priceSmall) {
        return priceLarge != 0 ? priceLarge : priceSmall;
    }

    @Override
    public void add(Long itemId, ItemType itemType) {
        switch (itemType) {
            case BEER:
                BeerInfoDto beer = beerService.getBeerById(itemId);
                if (beer == null) {
                    log.error("Beer with ID {} not found", itemId);
                    throw new ResourceNotFoundException(RESOURCE_NOT_FOUND, "");
                }
                log.debug("Add beer to cart {}", beer);
                cart.add(itemId, itemType);
                break;
            case SNACK:
                SnackInfoDto snack = snackService.getSnackById(itemId);
                if (snack == null) {
                    log.error("Snack with ID {} not found", itemId);
                    throw new ResourceNotFoundException(RESOURCE_NOT_FOUND, "");
                }
                log.debug("Add snack to cart {}", snack);
                cart.add(itemId, itemType);
                break;
            case PRODUCT_BUNDLE:
                ProductBundleInfoDto bundle = bundleService.getProductBundleById(itemId);
                if (bundle == null) {
                    log.error("Product bundle with ID {} not found", itemId);
                    throw new ResourceNotFoundException(RESOURCE_NOT_FOUND, "");
                }
                log.debug("Add product bundle to cart {}", bundle);
                cart.add(itemId, itemType);
                break;
            case CIDER:
                CiderInfoDto cider = ciderService.getCiderById(itemId);
                if (cider == null) {
                    log.error("Cider with ID {} not found", itemId);
                    throw new ResourceNotFoundException(RESOURCE_NOT_FOUND, "");
                }
                log.debug("Add cider to cart {}", cider);
                cart.add(itemId, itemType);
                break;
            default:
                throw new IllegalArgumentException("Unsupported item type: " + itemType);
        }
    }

    @Override
    public void updateQuantity(Long itemId, int quantity, ItemType itemType) {
        log.debug("Updating item quantity in cart, itemId: {}, quantity: {}, itemType: {}", itemId, quantity, itemType);
        cart.updateQuantity(itemId, quantity, itemType);
    }
    @Override
    public void removeItem(Long itemId, ItemType itemType){
        log.debug("Removing item from cart, itemId: {}, itemType: {}", itemId, itemType);
        cart.remove(itemId, itemType);
    }

    @Override
    public void delete() {
        log.debug("Clear cart");
        cart.delete();
    }
}
