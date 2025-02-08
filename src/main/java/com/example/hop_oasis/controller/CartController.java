package com.example.hop_oasis.controller;

import com.example.hop_oasis.dto.*;
import com.example.hop_oasis.service.data.CartServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartServiceImpl cartService;

    @GetMapping("/{cartId}")
    public ResponseEntity<CartDto> find(@PathVariable Long cartId) {
        return ResponseEntity.ok().body(cartService.getAllItemsByCartId(cartId));
    }

    @PostMapping
    public ResponseEntity<CartItemDto> addItem(@Valid @RequestBody ItemRequestDto itemRequestDto,
                                               Authentication authentication) {
        return ResponseEntity.ok(cartService.create(itemRequestDto, authentication));
    }

    @PutMapping
    public ResponseEntity<CartDto> updateCart(@Valid @RequestBody CartUpdateRequestDto cartUpdateRequestDto) {
        CartDto dto = cartService.updateCart(cartUpdateRequestDto.getCartId(), cartUpdateRequestDto.getItems());
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/remove/{cartId}")
    public ResponseEntity<String> removeItem(@PathVariable("cartId") Long cartId,
                                             @RequestBody RemoveItemRequestDto removeItemRequestDto) {
        cartService.removeItem(cartId,
                removeItemRequestDto.getItemId(),
                removeItemRequestDto.getItemType(),
                removeItemRequestDto.getMeasureValue());
        return ResponseEntity.ok("Done");
    }

    @DeleteMapping("/clear/{cartId}")
    public ResponseEntity<String> clearCart(@PathVariable("cartId") Long cartId) {
        cartService.delete(cartId);
        return ResponseEntity.ok("Done");
    }
}
