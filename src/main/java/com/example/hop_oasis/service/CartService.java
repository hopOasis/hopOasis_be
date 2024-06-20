package com.example.hop_oasis.service;

import com.example.hop_oasis.dto.CartDto;
import com.example.hop_oasis.model.ItemType;

public interface CartService {
    CartDto getAllItems();

    void add(Long itemId, ItemType itemType);

    void updateQuantity(Long itemId, int quantity, ItemType itemType);

    void removeItem(Long itemId, ItemType itemType);

    void delete();
}
