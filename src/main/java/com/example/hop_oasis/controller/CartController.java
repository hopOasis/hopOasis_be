package com.example.hop_oasis.controller;

import com.example.hop_oasis.dto.CartDto;
import com.example.hop_oasis.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<CartDto> find() {
        return ResponseEntity.ok().body(cartService.getAllItems());
    }

    @PutMapping
    public String add(@RequestParam("beerId") Long beerId) {
        cartService.add(beerId);
        return "Done";
    }

    @PostMapping
    public ResponseEntity<String> updateQuantity(@RequestParam("beerId") Long beerId,
                                                 @RequestParam("quantity") int quantity) {
        cartService.updateQuantity(beerId, quantity);
        return ResponseEntity.ok().body("Done");
    }

    @DeleteMapping
    public ResponseEntity<String> delete() {
        cartService.delete();
        return ResponseEntity.ok().body("Done");
    }
}
