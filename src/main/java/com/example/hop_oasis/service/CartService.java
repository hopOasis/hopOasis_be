package com.example.hop_oasis.service;

import com.example.hop_oasis.dto.CartDto;
import com.example.hop_oasis.dto.CartItemDto;
import com.example.hop_oasis.model.ItemType;

import java.util.List;

public interface CartService {
    List<CartItemDto> createCartItems();
    CartDto getAllItems(List<CartItemDto> items);

    CartItemDto add(Long itemId,int quantity, ItemType itemType);

    CartItemDto updateQuantity(Long itemId, int quantity, ItemType itemType);

    void removeItem(Long itemId, ItemType itemType);

    void delete();
}