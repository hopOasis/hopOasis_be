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

import java.util.function.Function;


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
    private static int newQuantity;

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final BeerService beerService;
    private final SnackService snackService;
    private final ProductBundleService bundleService;
    private final CiderService ciderService;
    private final BeerOptionsRepository beerOptionsRepository;
    private final CiderOptionsRepository ciderOptionsRepository;
    private final SnackOptionsRepository snackOptionsRepository;
    private final ProductBundleOptionsRepository productBundleOptionsRepository;

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
        updateStockAfterCreating(itemRequestDto, itemRequestDto.getItemType());

        Cart cart = new Cart();
        cart = cartRepository.save(cart);
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setItemId(itemRequestDto.getItemId());
        cartItem.setItemType(itemRequestDto.getItemType());
        cartItem.setQuantity(itemRequestDto.getQuantity());
        cartItem.setMeasureValue(itemRequestDto.getMeasureValue());

        cartItemRepository.save(cartItem);

        return createCartItemDto(cartItem);
    }

    private void updateStockAfterCreating(ItemRequestDto itemRequestDto, ItemType itemType) {
        switch (itemType) {
            case BEER:
                updateBeerStockAfterCreating(itemRequestDto);
                break;
            case CIDER:
                updateCiderStockAfterCreating(itemRequestDto);
                break;
            case SNACK:
                updateSnackStockAfterCreating(itemRequestDto);
                break;
            case PRODUCT_BUNDLE:
                updateBundleStockAfterCreating(itemRequestDto);
                break;
            default:
                throw new IllegalArgumentException("Unsupported item type: " + itemType);

        }

    }

    private void updateBeerStockAfterCreating(ItemRequestDto itemRequestDto) {
        Optional<BeerOptions> optionalBeerOptions = beerOptionsRepository.findByBeerIdAndVolume(
                itemRequestDto.getItemId(), itemRequestDto.getMeasureValue());
        BeerOptions beerOptions = optionalBeerOptions
                .orElseThrow(() -> new ResourceNotFoundException("Beer options not found for this beer", ""));
        if (beerOptions.getQuantity() < itemRequestDto.getQuantity()) {
            throw new IllegalArgumentException("Not enough beer in stock");
        }

        newQuantity = beerOptions.getQuantity() - itemRequestDto.getQuantity();
        beerOptions.setQuantity(newQuantity);
        beerOptionsRepository.save(beerOptions);

    }

    private void updateCiderStockAfterCreating(ItemRequestDto itemRequestDto) {
        Optional<CiderOptions> optionalCiderOptions = ciderOptionsRepository.findByCiderIdAndVolume(
                itemRequestDto.getItemId(), itemRequestDto.getMeasureValue());
        CiderOptions ciderOptions = optionalCiderOptions
                .orElseThrow(() -> new ResourceNotFoundException("Cider options not found for this cider", ""));
        if (ciderOptions.getQuantity() < itemRequestDto.getQuantity()) {
            throw new IllegalArgumentException("Not enough cider in stock");
        }

        newQuantity = ciderOptions.getQuantity() - itemRequestDto.getQuantity();
        ciderOptions.setQuantity(newQuantity);
        ciderOptionsRepository.save(ciderOptions);
    }

    private void updateSnackStockAfterCreating(ItemRequestDto itemRequestDto) {
        Optional<SnackOptions> optionalSnackOptions = snackOptionsRepository.findBySnackIdAndWeight(
                itemRequestDto.getItemId(), itemRequestDto.getMeasureValue());
        SnackOptions snackOptions = optionalSnackOptions
                .orElseThrow(() -> new ResourceNotFoundException("Snack options not found for this snack", ""));
        if (snackOptions.getQuantity() < itemRequestDto.getQuantity()) {
            throw new IllegalArgumentException("Not enough snack in stock");
        }
        newQuantity = snackOptions.getQuantity() - itemRequestDto.getQuantity();
        snackOptions.setQuantity(newQuantity);
        snackOptionsRepository.save(snackOptions);



    }
    private void updateBundleStockAfterCreating(ItemRequestDto itemRequestDto) {
        Optional<ProductBundleOptions> optionalProductBundleOptions = productBundleOptionsRepository
                .findByProductBundleId(
                itemRequestDto.getItemId());
        ProductBundleOptions productBundleOptions = optionalProductBundleOptions
                .orElseThrow(() -> new ResourceNotFoundException("Bundle options not found for this bundle", ""));
        if (productBundleOptions.getQuantity() < itemRequestDto.getQuantity()) {
            throw new IllegalArgumentException("Not enough bundle in stock");
        }
        newQuantity = productBundleOptions.getQuantity() - itemRequestDto.getQuantity();
        productBundleOptions.setQuantity(newQuantity);
        productBundleOptionsRepository.save(productBundleOptions);
    }

    @Transactional
    @Override
    public CartDto updateCart(Long cartId, List<ItemRequestDto> items) {
        Optional<Cart> optionalCart = cartRepository.findById(cartId);
        Cart cart = optionalCart
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found", ""));

        for (ItemRequestDto item : items) {
            CartItem cartItem = cartItemRepository.findByCartIdAndItemIdAndMeasureValueAndItemType(
                    cartId, item.getItemId(), item.getMeasureValue(), item.getItemType());


            if (cartItem == null) {
                cartItem = new CartItem();
                cartItem.setCart(cart);
                cartItem.setItemId(item.getItemId());
                cartItem.setItemType(item.getItemType());
                cartItem.setMeasureValue(item.getMeasureValue());
                cartItem.setQuantity(0);
            }

            newQuantity = item.getQuantity();
            int currentQuantity = cartItem.getQuantity();

            if (item.getItemType() == ItemType.BEER) {
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
                beerOptionsRepository.save(beerOptions);
            } else if (item.getItemType() == ItemType.CIDER) {
                Optional<CiderOptions> optionalCiderOptions = ciderOptionsRepository.findByCiderIdAndVolume(
                        item.getItemId(), item.getMeasureValue());
                CiderOptions ciderOptions = optionalCiderOptions
                        .orElseThrow(() -> new ResourceNotFoundException("Cider options not found for this cider", ""));
                if (newQuantity > currentQuantity) {
                    int quantityToDecrease = newQuantity - currentQuantity;
                    if (ciderOptions.getQuantity() < quantityToDecrease) {
                        throw new IllegalArgumentException("Not enough cider in stock");
                    }
                    ciderOptions.setQuantity(ciderOptions.getQuantity() - quantityToDecrease);
                } else if (newQuantity < currentQuantity) {
                    int quantityToIncrease = currentQuantity - newQuantity;
                    ciderOptions.setQuantity(ciderOptions.getQuantity() + quantityToIncrease);
                }
                ciderOptionsRepository.save(ciderOptions);

            } else if (item.getItemType() == ItemType.SNACK) {
                Optional<SnackOptions> optionalSnackOptions = snackOptionsRepository.findBySnackIdAndWeight(
                        item.getItemId(), item.getMeasureValue());
                SnackOptions snackOptions = optionalSnackOptions
                        .orElseThrow(() -> new ResourceNotFoundException("Snack options not found for this snack", ""));
                if (newQuantity > currentQuantity) {
                    int quantityToDecrease = newQuantity - currentQuantity;
                    if (snackOptions.getQuantity() < quantityToDecrease) {
                        throw new IllegalArgumentException("Not enough snack in stock");
                    }
                    snackOptions.setQuantity(snackOptions.getQuantity() - quantityToDecrease);
                } else if (newQuantity < currentQuantity) {
                    int quantityToIncrease = currentQuantity - newQuantity;
                    snackOptions.setQuantity(snackOptions.getQuantity() + quantityToIncrease);
                }

                snackOptionsRepository.save(snackOptions);
            } else if (item.getItemType() == ItemType.PRODUCT_BUNDLE) {
                Optional<ProductBundleOptions> optionalProductBundleOptions = productBundleOptionsRepository
                        .findByProductBundleId(item.getItemId());
                ProductBundleOptions productBundleOptions = optionalProductBundleOptions
                        .orElseThrow(() -> new ResourceNotFoundException("Options not found for this bundle", ""));
                if (newQuantity > currentQuantity) {
                    int quantityToDecrease = newQuantity - currentQuantity;
                    if (productBundleOptions.getQuantity() < quantityToDecrease) {
                        throw new IllegalArgumentException("Not enough bundle in stock");
                    }
                    productBundleOptions.setQuantity(productBundleOptions.getQuantity() - quantityToDecrease);
                } else if (newQuantity < currentQuantity) {
                    int quantityToIncrease = currentQuantity - newQuantity;
                    productBundleOptions.setQuantity(productBundleOptions.getQuantity() + quantityToIncrease);
                }
                productBundleOptionsRepository.save(productBundleOptions);
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
        if (cartItems.isEmpty()) {
            throw new ResourceNotFoundException("Cart item not found", "");
        }
        for (CartItem cartItem : cartItems) {
            updateStockAfterRemove(cartItem, itemType);
            cartItemRepository.delete(cartItem);

        }

    }

    private void updateStockAfterRemove(CartItem cartItem, ItemType itemType) {
        switch (itemType) {
            case BEER:
                updateBeerStockAfterRemove(cartItem);
                break;
            case CIDER:
                updateCiderStockAfterRemove(cartItem);
                break;
            case SNACK:
                updateSnackStockAfterRemove(cartItem);
                break;
            case PRODUCT_BUNDLE:
                updateBundleStockAfterRemove(cartItem);
                break;
            default:
                throw new IllegalArgumentException("Unsupported item type: " + itemType);
        }

    }

    private void updateBeerStockAfterRemove(CartItem cartItem) {
        Optional<BeerOptions> optionalBeerOptions = beerOptionsRepository.findByBeerIdAndVolume(
                cartItem.getItemId(), cartItem.getMeasureValue());
        BeerOptions beerOptions = optionalBeerOptions
                .orElseThrow(() -> new ResourceNotFoundException("Beer options not found for this beer", ""));
        newQuantity = beerOptions.getQuantity() + cartItem.getQuantity();
        beerOptions.setQuantity(newQuantity);
        beerOptionsRepository.save(beerOptions);
    }

    private void updateCiderStockAfterRemove(CartItem cartItem) {
        Optional<CiderOptions> optionalCiderOptions = ciderOptionsRepository.findByCiderIdAndVolume(
                cartItem.getItemId(), cartItem.getMeasureValue());
        CiderOptions ciderOptions = optionalCiderOptions
                .orElseThrow(() -> new ResourceNotFoundException("Cider options not found for this cider", ""));
        newQuantity = ciderOptions.getQuantity() + cartItem.getQuantity();
        ciderOptions.setQuantity(newQuantity);
        ciderOptionsRepository.save(ciderOptions);

    }

    private void updateSnackStockAfterRemove(CartItem cartItem) {
        Optional<SnackOptions> optionalSnackOptions = snackOptionsRepository.findBySnackIdAndWeight(
                cartItem.getItemId(), cartItem.getMeasureValue());
        SnackOptions snackOptions = optionalSnackOptions
                .orElseThrow(() -> new ResourceNotFoundException("Snack options not found for this snack", ""));
        newQuantity = snackOptions.getQuantity() + cartItem.getQuantity();
        snackOptions.setQuantity(newQuantity);
        snackOptionsRepository.save(snackOptions);
    }

    private void updateBundleStockAfterRemove(CartItem cartItem) {
        Optional<ProductBundleOptions> optionalProductBundleOptions = productBundleOptionsRepository.
                findByProductBundleId(cartItem.getId());
        ProductBundleOptions productBundleOptions = optionalProductBundleOptions
                .orElseThrow(() -> new ResourceNotFoundException("Bundle options not found for this bundle", ""));
        newQuantity = productBundleOptions.getQuantity() + cartItem.getQuantity();
        productBundleOptions.setQuantity(newQuantity);
        productBundleOptionsRepository.save(productBundleOptions);


    }


    @Override
    public void delete(Long cartId) {
        log.debug("Clear cart");
        List<CartItem> cartItems = cartItemRepository.findByCartId(cartId);
        for (CartItem cartItem : cartItems) {
            updateStockAfterRemove(cartItem, cartItem.getItemType());
        }
        cartItemRepository.deleteByCartId(cartId);
        cartRepository.deleteById(cartId);

    }

    private CartItemDto createCartItemDto(CartItem cartItem) {
        CartItemDto dto = new CartItemDto();
        dto.setCartId(cartItem.getCart().getId());
        dto.setItemId(cartItem.getItemId());
        dto.setQuantity(cartItem.getQuantity());

        switch (cartItem.getItemType()) {
            case BEER -> {
                BeerInfoDto beerInfo = beerService.getBeerById(cartItem.getItemId());
                if (beerInfo != null) {
                    dto.setItemTitle(beerInfo.getBeerName());
                    if (cartItem.getMeasureValue() != null) {
                        BeerOptionsDto selectedVolume = chooseOptionByCriteria(
                                beerInfo.getOptions(), cartItem.getMeasureValue(), BeerOptionsDto::getVolume);
                        if (selectedVolume != null) {
                            dto.setPricePerItem(selectedVolume.getPrice());
                        }
                    }
                }
            }
            case CIDER -> {
                CiderInfoDto ciderInfo = ciderService.getCiderById(cartItem.getItemId());
                if (ciderInfo != null) {
                    dto.setItemTitle(ciderInfo.getCiderName());
                    if (cartItem.getMeasureValue() != null) {
                        CiderOptionsDto selectedVolume = chooseOptionByCriteria(
                                ciderInfo.getOptions(), cartItem.getMeasureValue(), CiderOptionsDto::getVolume);
                        if (selectedVolume != null) {
                            dto.setPricePerItem(selectedVolume.getPrice());
                        }
                    }
                }
            }
            case SNACK -> {
                SnackInfoDto snackInfo = snackService.getSnackById(cartItem.getItemId());
                if (snackInfo != null) {
                    dto.setItemTitle(snackInfo.getSnackName());
                    if (cartItem.getMeasureValue() != null) {
                        SnackOptionsDto selectedWeight = chooseOptionByCriteria(
                                snackInfo.getOptions(), cartItem.getMeasureValue(), SnackOptionsDto::getWeight);
                        if (selectedWeight != null) {
                            dto.setPricePerItem(selectedWeight.getPrice());
                        }
                    }
                }
            }
            case PRODUCT_BUNDLE -> {
                ProductBundleInfoDto bundleInfo = bundleService.getProductBundleById(cartItem.getItemId());
                if (bundleInfo != null) {
                    dto.setItemTitle(bundleInfo.getName());
                    ProductBundleOptionsDto selectedOption = new ProductBundleOptionsDto();
                    dto.setQuantity(selectedOption.getQuantity());
                }
            }

            default -> throw new ResourceNotFoundException(RESOURCE_NOT_FOUND, "");
        }
        dto.setTotalCost(Rounder.roundValue(dto.getQuantity() * dto.getPricePerItem()));
        return dto;
    }


    public <T> T chooseOptionByCriteria(List<T> options, double criteriaValue, Function<T, Double> getCriteriaFunction) {
        return options.stream()
                .filter(option -> getCriteriaFunction.apply(option).equals(criteriaValue))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Option not found with criteria value:"  + criteriaValue,
                        ""));
    }
    // З цим методом також пробував, не шукає по кількості
    public <T> T chooseOptionByIntCriteria(List<T> options, int criteriaValue, Function<T, Integer> getCriteriaFunction) {
        return options.stream()
                .filter(option -> getCriteriaFunction.apply(option) == criteriaValue)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Option not found with criteria value: " + criteriaValue, ""));
    }



}
