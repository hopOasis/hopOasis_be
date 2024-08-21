package com.example.hop_oasis.service.data;

import com.example.hop_oasis.dto.*;
import com.example.hop_oasis.model.*;
import com.example.hop_oasis.repository.*;
import com.example.hop_oasis.handler.exception.ResourceNotFoundException;
import com.example.hop_oasis.service.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import static com.example.hop_oasis.handler.exception.message.ExceptionMessage.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final BeerService beerService;
    private final SnackService snackService;
    private final ProductBundleService bundleService;
    private final CiderService ciderService;

    @Override
    public CartDto getAllItemsByCartId(Long cartId) {
        List<CartItemDto> items = new ArrayList<>();
        List<CartItem> cartItems = cartItemRepository.findByCartId(cartId);
        if (cartItems.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } else {

            for (CartItem cartItem : cartItems) {
                CartItemDto dto = createCartItemDto(cartItem);
                items.add(dto);
            }

            CartDto result = new CartDto();
            result.setItems(items);
            result.setPriceForAll(items.stream()
                    .map(CartItemDto::getTotalCost)
                    .reduce(BigDecimal.ZERO, BigDecimal::add));
            log.debug("Return cart with all items: {}", result);
            return result;
        }
    }

    @Override
    public CartItemDto create(ItemRequestDto itemRequestDto) {
        Cart cart = new Cart();
        cart = cartRepository.save(cart);
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setItemId(itemRequestDto.getItemId());
        cartItem.setItemType(itemRequestDto.getItemType());
        cartItem.setQuantity(itemRequestDto.getQuantity());

        cartItemRepository.save(cartItem);

        return createCartItemDto(cartItem);
    }


    @Override
    public CartDto updateCart(Long cartId, List<ItemRequestDto> items) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found", ""));

        for (ItemRequestDto item : items) {
            List<CartItem> cartItems = cartItemRepository.findByCartIdAndItemIdAndItemType(cartId
                    , item.getItemId(), item.getItemType());
            CartItem cartItem;

            if (cartItems.isEmpty()) {
                cartItem = new CartItem();
                cartItem.setCart(cart);
                cartItem.setItemId(item.getItemId());
                cartItem.setItemType(item.getItemType());
                cartItem.setQuantity(item.getQuantity());
            } else {
                cartItem = cartItems.getFirst();
                cartItem.setQuantity(item.getQuantity());
            }

            cartItemRepository.save(cartItem);
        }
        return getAllItemsByCartId(cartId);
    }


    @Override
    public void removeItem(Long cartId, Long itemId, ItemType itemType) {
        List<CartItem> cartItems = cartItemRepository.findByCartIdAndItemIdAndItemType(cartId, itemId, itemType);
        if (!cartItems.isEmpty()) {
            for (CartItem cartItem : cartItems) {
                cartItemRepository.delete(cartItem);
            }
        } else {
            throw new ResourceNotFoundException(RESOURCE_NOT_FOUND, "");
        }
    }


    @Override
    public void delete(Long cartId) {
        log.debug("Clear cart");
        cartItemRepository.deleteByCartId(cartId);
    }

    private CartItemDto createCartItemDto(CartItem cartItem) {
        CartItemDto dto = new CartItemDto();
        dto.setCartId((cartItem.getCart().getId()));
        dto.setItemId(cartItem.getItemId());
        dto.setQuantity(cartItem.getQuantity());

        switch (cartItem.getItemType()) {
            case BEER:
                BeerInfoDto beerInfo = beerService.getBeerById(cartItem.getItemId());
                if (beerInfo != null) {
                    dto.setItemTitle(beerInfo.getBeerName());
                    dto.setPricePerItem(determinePrice(beerInfo.getPriceLarge(), beerInfo.getPriceSmall()));
                }
                break;
            case SNACK:
                SnackInfoDto snackInfo = snackService.getSnackById(cartItem.getItemId());
                if (snackInfo != null) {
                    dto.setItemTitle(snackInfo.getSnackName());
                    dto.setPricePerItem(determinePrice(snackInfo.getPriceLarge(), snackInfo.getPriceSmall()));
                }
                break;
            case PRODUCT_BUNDLE:
                ProductBundleInfoDto bundleInfo = bundleService.getProductBundleById(cartItem.getItemId());
                if (bundleInfo != null) {
                    dto.setItemTitle(bundleInfo.getName());
                    dto.setPricePerItem(bundleInfo.getPrice());
                }
                break;
            case CIDER:
                CiderInfoDto ciderInfo = ciderService.getCiderById(cartItem.getItemId());
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
