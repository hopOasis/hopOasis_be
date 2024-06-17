package com.example.hop_oasis.controller;

import com.example.hop_oasis.dto.CartDto;
import com.example.hop_oasis.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;

    @GetMapping
    public ResponseEntity<CartDto> find() {
        return ResponseEntity.ok().body(cartService.getAllItems());
    }

    @PutMapping
    public ResponseEntity<Void> add(@RequestParam("beerId") Long beerId) {
        cartService.add(beerId);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<Void> updateQuantity(@RequestParam("beerId") Long beerId,
                                                 @RequestParam("quantity") int quantity) {
        cartService.updateQuantity(beerId, quantity);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> delete() {
        cartService.delete();
        return ResponseEntity.ok().build();
    }
}
