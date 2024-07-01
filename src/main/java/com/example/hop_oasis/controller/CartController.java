package com.example.hop_oasis.controller;

import com.example.hop_oasis.dto.ItemRequestDto;
import com.example.hop_oasis.model.ItemType;
import com.example.hop_oasis.dto.CartDto;
import com.example.hop_oasis.service.CartService;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<Void> add(@RequestBody ItemRequestDto itemRequest) {
        cartService.add(itemRequest.getItemId(), itemRequest.getItemType());
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<Void> updateQuantity(@RequestBody ItemRequestDto itemRequest) {
        cartService.updateQuantity(itemRequest.getItemId(), itemRequest.getQuantity(), itemRequest.getItemType());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/item")
    public ResponseEntity<Void> removeItem(@RequestParam("itemId") Long itemId,
                                           @RequestParam("itemType") ItemType itemType) {
        cartService.removeItem(itemId, itemType);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> delete() {
        cartService.delete();
        return ResponseEntity.ok().build();
    }
}
