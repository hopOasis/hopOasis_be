package com.example.hop_oasis.controller;

import com.example.hop_oasis.dto.CartItemDto;
import com.example.hop_oasis.dto.ItemRequestDto;
import com.example.hop_oasis.model.ItemType;
import com.example.hop_oasis.dto.CartDto;
import com.example.hop_oasis.service.CartService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
@SessionAttributes("cart")
public class CartController {
    private final CartService cartService;
    @ModelAttribute("cart")
    public List<CartItemDto> createCartItems() {
        return cartService.createCartItems();
    }
    @GetMapping("/session-id")
    public ResponseEntity<String>getSessionId(HttpSession session) {
        return ResponseEntity.ok().body(session.getId());
    }

    @GetMapping
    public ResponseEntity<CartDto> find() {
        return ResponseEntity.ok().body(cartService.getAllItems(createCartItems()));
    }

//    @PostMapping("/items")
//    public ResponseEntity<CartItemDto> add(@RequestBody ItemRequestDto itemRequest) {
//        CartItemDto dto = cartService.add(itemRequest.getItemId(), itemRequest.getQuantity(), itemRequest.getItemType());
//        return ResponseEntity.ok(dto);
//    }
    @GetMapping("/items")public ResponseEntity<CartItemDto> addItem(
            @RequestParam("itemId") Long itemId,
            @RequestParam("quantity") int quantity,
            @RequestParam("itemType") ItemType itemType) {
        CartItemDto dto = cartService.add(itemId, quantity, itemType);
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