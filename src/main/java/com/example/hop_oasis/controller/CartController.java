package com.example.hop_oasis.controller;

import com.example.hop_oasis.dto.CartDto;
import com.example.hop_oasis.dto.CartItemDto;
import com.example.hop_oasis.dto.CartUpdateRequestDto;
import com.example.hop_oasis.dto.ItemRequestDto;
import com.example.hop_oasis.model.ItemType;
import com.example.hop_oasis.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping("/{cartId}")
    public ResponseEntity<CartDto> find(@PathVariable Long cartId) {
        return ResponseEntity.ok().body(cartService.getAllItemsByCartId(cartId));
    }

    @PostMapping
    public ResponseEntity<CartItemDto> addItem(@RequestBody ItemRequestDto itemRequestDto) {
        CartItemDto dto = cartService.add(itemRequestDto.getCartId(),
                itemRequestDto.getItemId(), itemRequestDto.getQuantity(), itemRequestDto.getItemType());
        return ResponseEntity.ok(dto);
    }

    @PutMapping
    public ResponseEntity<CartDto> updateCart(@RequestBody CartUpdateRequestDto cartUpdateRequestDto) {
        CartDto dto = cartService.updateCart(cartUpdateRequestDto.getCartId(), cartUpdateRequestDto.getItems());
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/remove/{cartId}")
    public ResponseEntity<String> removeItem(@PathVariable ("cartId") Long cartId,
                                             @RequestParam("itemId") Long itemId,
                                             @RequestParam("itemType") ItemType itemType) {
        cartService.removeItem(cartId, itemId, itemType);
        return ResponseEntity.ok("Done");
    }

    @DeleteMapping("/clear/{cartId}")
    public ResponseEntity<String> clearCart(@PathVariable("cartId") Long cartId) {
        cartService.delete(cartId);
        return ResponseEntity.ok("Done");
    }
}
