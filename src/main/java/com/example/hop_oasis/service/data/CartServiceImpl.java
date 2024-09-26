package com.example.hop_oasis.service.data;

import com.example.hop_oasis.dto.*;
import com.example.hop_oasis.model.*;
import com.example.hop_oasis.repository.*;
import com.example.hop_oasis.handler.exception.ResourceNotFoundException;
import com.example.hop_oasis.service.*;
import com.example.hop_oasis.utils.Rounder;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    private final BeerOptionsRepository beerOptionsRepository;

    @Override
    public CartDto getAllItemsByCartId(Long cartId) {
        List<CartItemDto> items = new ArrayList<>();
        List<CartItem> cartItems = cartItemRepository.findByCartId(cartId);
        if (cartItems.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } else {

            for (CartItem cartItem : cartItems) {
                CartItemDto dto = createCartItemDto(cartItem, cartItem.getMeasureValue());
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
        Optional<BeerOptions> optionalBeerOptions = beerOptionsRepository.findByBeerIdAndVolume(
                itemRequestDto.getItemId(), itemRequestDto.getMeasureValue());
        BeerOptions beerOptions = optionalBeerOptions
                .orElseThrow(() -> new ResourceNotFoundException("Beer options not found for this beer", ""));
        if (beerOptions.getQuantity() < itemRequestDto.getQuantity()) {
            throw new IllegalArgumentException("Not enough beer in stock");
        }

        int newQuantity = beerOptions.getQuantity() - itemRequestDto.getQuantity();
        beerOptions.setQuantity(newQuantity);
        beerOptionsRepository.save(beerOptions);

        Cart cart = new Cart();
        cart = cartRepository.save(cart);
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setItemId(itemRequestDto.getItemId());
        cartItem.setItemType(itemRequestDto.getItemType());
        cartItem.setQuantity(itemRequestDto.getQuantity());
        cartItem.setMeasureValue(itemRequestDto.getMeasureValue());


        cartItemRepository.save(cartItem);

        return createCartItemDto(cartItem, itemRequestDto.getMeasureValue());
    }

    @Transactional
    @Override
    public CartDto updateCart(Long cartId, List<ItemRequestDto> items) {
        Optional<Cart> optionalCart = cartRepository.findById(cartId);
        Cart cart = optionalCart
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found", ""));

        for (ItemRequestDto item : items) {
            Optional<CartItem> optionalCartItem = cartItemRepository.findByCartIdAndItemIdAndMeasureValueAndItemType(
                    cartId, item.getItemId(), item.getMeasureValue(), item.getItemType());
            CartItem cartItem = optionalCartItem
                    .orElseThrow(() -> new ResourceNotFoundException("Item not found", ""));

            if (cartItem == null) {
                cartItem = new CartItem();
                cartItem.setCart(cart);
                cartItem.setItemId(item.getItemId());
                cartItem.setItemType(item.getItemType());
                cartItem.setMeasureValue(item.getMeasureValue());
                cartItem.setQuantity(0);
            }

            int newQuantity = item.getQuantity();
            int currentQuantity = cartItem.getQuantity();

            Optional<BeerOptions> optionalBeerOptions = beerOptionsRepository.findByBeerIdAndVolume(
                    item.getItemId(), item.getMeasureValue());

            BeerOptions beerOptions = optionalBeerOptions
                    .orElseThrow(() -> new ResourceNotFoundException("Beer options not found for this beer", ""));

            if (newQuantity > currentQuantity) {
                int quantityToDecrease = newQuantity - currentQuantity;
                if (beerOptions.getQuantity() < quantityToDecrease) {
                    throw new IllegalArgumentException("Not enough beer in stock");
                }
                beerOptions.setQuantity(beerOptions.getQuantity() - quantityToDecrease);
            } else if (newQuantity < currentQuantity) {
                int quantityToIncrease = currentQuantity - newQuantity;
                beerOptions.setQuantity(beerOptions.getQuantity() + quantityToIncrease);
            }

            cartItem.setQuantity(newQuantity);
            cartItemRepository.save(cartItem);
        }

        return getAllItemsByCartId(cartId);
    }


    @Override
    public void removeItem(Long cartId, Long itemId, ItemType itemType, double measureValue) {
        List<CartItem> cartItems = cartItemRepository.findByCartIdAndItemIdAndItemTypeAndMeasureValue(
                cartId,
                itemId,
                itemType,
                measureValue);
        if (!cartItems.isEmpty()) {
            for (CartItem cartItem : cartItems) {
                Optional<BeerOptions> optionalBeerOptions = beerOptionsRepository.findByBeerIdAndVolume(
                        cartItem.getItemId(),
                        cartItem.getMeasureValue());
                BeerOptions beerOptions = optionalBeerOptions
                        .orElseThrow(() -> new ResourceNotFoundException("Beer options not found for this beer", ""));
                if (beerOptions != null) {
                    int newQuantity = beerOptions.getQuantity() + cartItem.getQuantity();
                    beerOptions.setQuantity(newQuantity);
                    beerOptionsRepository.save(beerOptions);
                }
                cartItemRepository.delete(cartItem);
            }
        } else {
            throw new ResourceNotFoundException(RESOURCE_NOT_FOUND, "");
        }
    }


    @Override
    public void delete(Long cartId) {
        log.debug("Clear cart");
        List<CartItem> cartItems = cartItemRepository.findByCartId(cartId);
        for (CartItem cartItem : cartItems) {
            Optional<BeerOptions> optionalBeerOptions = beerOptionsRepository.findByBeerIdAndVolume(
                    cartItem.getItemId(),
                    cartItem.getMeasureValue());
            BeerOptions beerOptions = optionalBeerOptions
                    .orElseThrow(() -> new IllegalArgumentException("Beer options not found for this beer"));

            if (beerOptions != null) {
                int newQuantity = beerOptions.getQuantity() + cartItem.getQuantity();
                beerOptions.setQuantity(newQuantity);
                beerOptionsRepository.save(beerOptions);
            }

        }
        cartItemRepository.deleteByCartId(cartId);
    }

    private CartItemDto createCartItemDto(CartItem cartItem, double measureValue) {
        CartItemDto dto = new CartItemDto();
        dto.setCartId(cartItem.getCart().getId());
        dto.setItemId(cartItem.getItemId());
        dto.setQuantity(cartItem.getQuantity());

        switch (cartItem.getItemType()) {
            case BEER -> {
                BeerInfoDto beerInfo = beerService.getBeerById(cartItem.getItemId());
                if (beerInfo != null) {
                    dto.setItemTitle(beerInfo.getBeerName());
                    BeerOptionsDto selectedVolume = chooseVolume(beerInfo.getOptions(), cartItem.getMeasureValue());
                    if (selectedVolume != null) {
                        dto.setPricePerItem(selectedVolume.getPrice());
                    }
                }
            }
           /* case SNACK -> {
                SnackInfoDto snackInfo = snackService.getSnackById(cartItem.getItemId());
                if (snackInfo != null) {
                    dto.setItemTitle(snackInfo.getSnackName());
                    dto.setPricePerItem(chooseMeasureValue(cartItem, measureValue).getPricePerItem());
                }
            }
            case PRODUCT_BUNDLE -> {
                ProductBundleInfoDto bundleInfo = bundleService.getProductBundleById(cartItem.getItemId());
                if (bundleInfo != null) {
                    dto.setItemTitle(bundleInfo.getName());
                    dto.setPricePerItem(bundleInfo.getPrice());
                }
            }
            case CIDER -> {
                CiderInfoDto ciderInfo = ciderService.getCiderById(cartItem.getItemId());
                if (ciderInfo != null) {
                    dto.setItemTitle(ciderInfo.getCiderName());
                    dto.setPricePerItem(chooseMeasureValue(cartItem, measureValue).getPricePerItem());
                }
            }*/
            default -> throw new ResourceNotFoundException(RESOURCE_NOT_FOUND, "");
        }
        dto.setTotalCost(Rounder.roundValue(dto.getQuantity() * dto.getPricePerItem()));
        return dto;
    }


    private BeerOptionsDto chooseVolume(List<BeerOptionsDto> volumes, double measureValue) {
        return volumes.stream()
                .filter(volume -> volume.getVolume() == measureValue)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Volume not found", measureValue));
    }

}
