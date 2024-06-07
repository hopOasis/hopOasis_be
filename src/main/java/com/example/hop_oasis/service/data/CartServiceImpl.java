package com.example.hop_oasis.service.data;

import com.example.hop_oasis.component.Cart;
import com.example.hop_oasis.dto.BeerDto;
import com.example.hop_oasis.dto.BeerInfoDto;
import com.example.hop_oasis.dto.CartDto;
import com.example.hop_oasis.dto.CartItemDto;
import com.example.hop_oasis.service.CartService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Log4j2
public class CartServiceImpl implements CartService {
    private final Cart cart;
    private final BeerServiceImpl beerService;

    @Autowired
    public CartServiceImpl(Cart cart, BeerServiceImpl beerService) {
        this.cart = cart;
        this.beerService = beerService;
    }

    @Override
    public CartDto find() {
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
                    cartItemDto.setValue(BigDecimal.valueOf(beers.get(key))
                            .multiply(BigDecimal.valueOf(beerInfoDto.getPriceSmall())));
                    cartItemDto.setValue(BigDecimal.valueOf(beers.get(key))
                            .multiply(BigDecimal.valueOf(beerInfoDto.getPriceLarge())));
                    return cartItemDto;
                }).toList();
        CartDto result = new CartDto();
        result.setItems(items);
        result.setTotalCost(
                items.stream()
                        .map(it -> it.getValue())
                        .reduce((o1, o2) -> o1.add(o2))
                        .orElse(BigDecimal.ZERO)
        );
        return result;

    }

    @Override
    public void add(Long beerId) {
        cart.add(beerId);
    }

    @Override
    public void updateQuantity(Long beerId, int quantity) {
        cart.updateQuantity(beerId, quantity);
    }

    @Override
    public void delete() {
        cart.delete();
    }
}
