package com.example.hop_oasis.controller;

import com.example.hop_oasis.dto.CartItemDto;
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

    @GetMapping(produces = "application/json")
    public ResponseEntity<CartDto> find() {
        return ResponseEntity.ok().body(cartService.getAllItems());
    }

    @PostMapping(value = "/items",consumes = "application/json")
    public ResponseEntity<CartItemDto> add(@RequestBody ItemRequestDto itemRequest) {
        CartItemDto dto = cartService.add(itemRequest.getItemId(), itemRequest.getQuantity(), itemRequest.getItemType());
        return ResponseEntity.ok(dto);
    }

    @PutMapping
    public ResponseEntity<CartItemDto> updateQuantity(@RequestBody ItemRequestDto itemRequest) {
        CartItemDto dto = cartService.updateQuantity(itemRequest.getItemId(), itemRequest.getQuantity(), itemRequest.getItemType());
        return ResponseEntity.ok(dto);
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