package com.example.hop_oasis.controller;

import com.example.hop_oasis.dto.CartDto;
import com.example.hop_oasis.dto.CartItemDto;
import com.example.hop_oasis.dto.ItemRequestDto;
import com.example.hop_oasis.model.ItemType;
import com.example.hop_oasis.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@Log4j2
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<CartDto> find() {
        return ResponseEntity.ok().body(cartService.getAllItems());
    }

    @PostMapping("/add")
    public ResponseEntity<CartItemDto> addItem(@RequestBody ItemRequestDto itemRequestDto) {
        CartItemDto dto = cartService.add(itemRequestDto.getItemId(), itemRequestDto.getQuantity(), itemRequestDto.getItemType());
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/update")
    public ResponseEntity<CartItemDto> updateItemQuantity(@RequestBody ItemRequestDto itemRequestDto) {
        CartItemDto updatedItem = cartService.updateQuantity(itemRequestDto.getItemId(), itemRequestDto.getQuantity(), itemRequestDto.getItemType());

        return ResponseEntity.ok(updatedItem);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<Void> removeItem(@RequestParam Long itemId, @RequestParam ItemType itemType) {
        cartService.removeItem(itemId, itemType);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart() {
        cartService.delete();
        return ResponseEntity.noContent().build();
    }
}
