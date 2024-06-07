package com.example.hop_oasis.controller;

import com.example.hop_oasis.dto.CartDto;
import com.example.hop_oasis.service.CartService;
import com.example.hop_oasis.service.data.CartServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public CartDto find() {
        return cartService.find();
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public void add(@RequestParam("beerId") Long beerId) {
        cartService.add(beerId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void updateQuantity(@RequestParam("beerId") Long beerId,
                               @RequestParam("quantity") int quantity) {
        cartService.updateQuantity(beerId, quantity);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void delete() {
        cartService.delete();
    }
}
