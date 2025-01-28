package com.example.hop_oasis.controller;

import com.example.hop_oasis.dto.*;
import com.example.hop_oasis.service.data.CartServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartServiceImpl cartService;

    // To retrieve all items in a cart identified by its cartId
    @GetMapping("/{cartId}")
    public ResponseEntity<CartDto> find(@PathVariable Long cartId) {
        try {
            System.out.println("Fetching cart with ID: " + cartId);
            CartDto cartDto = cartService.getAllItemsByCartId(cartId);
            return ResponseEntity.ok(cartDto);
        } catch (CartNotFoundException ex) {
            return ResponseEntity.status(404).body(null); 
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(null); 
        }
    }

    // To add an item to the cart 
    @PostMapping
    public ResponseEntity<CartItemDto> addItem(@Valid @RequestBody ItemRequestDto itemRequestDto) {
        try {
            System.out.println("Adding item to cart: " + itemRequestDto);
            CartItemDto dto = cartService.create(itemRequestDto);
            return ResponseEntity.status(201).body(dto);
        } catch (CartNotFoundException ex) {
            return ResponseEntity.status(404).body(null); 
        } catch (InvalidItemException ex) {
            return ResponseEntity.status(400).body(null);
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(null); 
        }
    }

    // To update an existing cart using the CartID and a list of Item provided
    @PutMapping
    public ResponseEntity<CartDto> updateCart(@Valid @RequestBody CartUpdateRequestDto cartUpdateRequestDto) {
        try {
            System.out.println("Updating cart with ID: " + cartUpdateRequestDto.getCartId());
            CartDto updatedCart = cartService.updateCart(cartUpdateRequestDto.getCartId(), cartUpdateRequestDto.getItems());
            return ResponseEntity.ok(updatedCart);
        } catch (CartNotFoundException ex) {
            return ResponseEntity.status(404).body(null); 
        } catch (InvalidItemException ex) {
            return ResponseEntity.status(400).body(null); 
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // To remove specific item from the cart
    @DeleteMapping("/{cartId}/items") // Changed Endpoint name to follow RESTful principles. 
    public ResponseEntity<String> removeItem(@PathVariable("cartId") Long cartId,
                                         @RequestBody RemoveItemRequestDto removeItemRequestDto) {
        try {
            System.out.println("Removing item with ID: " + removeItemRequestDto.getItemId() + " from cart ID: " + cartId);
            cartService.removeItem(cartId,
                removeItemRequestDto.getItemId(),
                removeItemRequestDto.getItemType(),
                removeItemRequestDto.getMeasureValue());
            return ResponseEntity.ok("Item with ID " + removeItemRequestDto.getItemId() +
                                 " removed from cart ID " + cartId); // Enchanced Response
        } catch (CartNotFoundException ex) {
            return ResponseEntity.status(404).body("Cart not found: " + cartId);
        } catch (ItemNotFoundException ex) {
            return ResponseEntity.status(404).body("Item not found: " + removeItemRequestDto.getItemId());
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("An error occurred while removing the item.");
        }
    }
    
    // To clear the cart 
    @DeleteMapping("/{cartId}") // Changed the path from /clear/{cartId} to simply /{cartId}, as the DELETE HTTP method already implies clearing resource.
    public ResponseEntity<String> clearCart(@PathVariable("cartId") Long cartId) {
        try {
            System.out.println("Clearing cart with ID: " + cartId);
            cartService.delete(cartId);
            return ResponseEntity.ok("Cart with ID " + cartId + " cleared successfully"); // Improved Response Message
        } catch (CartNotFoundException ex) {
            return ResponseEntity.status(404).body("Cart not found: " + cartId);
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("An error occurred while clearing the cart.");
        }
    }

