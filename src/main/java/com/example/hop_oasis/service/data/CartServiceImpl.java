package com.example.hop_oasis.service.data;

import com.example.hop_oasis.component.Cart;
import com.example.hop_oasis.dto.BeerInfoDto;
import com.example.hop_oasis.dto.CartDto;
import com.example.hop_oasis.dto.CartItemDto;

import static com.example.hop_oasis.hendler.exception.message.ExceptionMessage.*;

import com.example.hop_oasis.hendler.exception.BeerNotFoundException;
import com.example.hop_oasis.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Log4j2
public class CartServiceImpl implements CartService {
    private final Cart cart;
    private final BeerServiceImpl beerService;


    @Override
    public CartDto getAllItems() {
        Map<Long, Integer> beers = cart.getBeers();
        List<CartItemDto> items = beers.keySet()
                .stream()
                .map(key -> {
                    BeerInfoDto beerInfoDto = beerService.getBeerById(key);
                    CartItemDto cartItemDto = new CartItemDto();
                    cartItemDto.setBeerTitle(beerInfoDto.getBeerName());
                    cartItemDto.setPricePerBeer(beerInfoDto.getPriceSmall());
                    cartItemDto.setPricePerBeer(beerInfoDto.getPriceLarge());
                    cartItemDto.setQuantity(beers.get(key));
                    cartItemDto.setTotalCost(BigDecimal.valueOf(beers.get(key))
                            .multiply(BigDecimal.valueOf(beerInfoDto.getPriceSmall())));
                    cartItemDto.setTotalCost(BigDecimal.valueOf(beers.get(key))
                            .multiply(BigDecimal.valueOf(beerInfoDto.getPriceLarge())));
                    return cartItemDto;
                }).toList();
        CartDto result = new CartDto();
        result.setItems(items);
        result.setPriceForAll(
                items.stream()
                        .map(it -> it.getTotalCost())
                        .reduce((o1, o2) -> o1.add(o2))
                        .orElse(BigDecimal.ZERO)
        );
        log.debug("Return cart with all the beer: {}", result);
        return result;

    }

    @Override
    public void add(Long beerId) {
        BeerInfoDto beer = beerService.getBeerById(beerId);
        if (beer == null) {
            throw new BeerNotFoundException(BEER_NOT_FOUND, beerId);
        }
        log.debug("Add beer to cart {}", beer);
        cart.add(beerId);
    }

    @Override
    public void updateQuantity(Long beerId, int quantity) {
        BeerInfoDto beer = beerService.getBeerById(beerId);
        if (beer == null) {
            throw new BeerNotFoundException(BEER_NOT_FOUND, beerId);
        }
        log.debug("Updated beer {} ,new quantity is {}", beer.getBeerName(), quantity);
        cart.updateQuantity(beerId, quantity);
    }

    @Override
    public void delete() {
        log.debug("Clear cart");
        cart.delete();
    }
}
