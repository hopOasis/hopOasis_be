package com.example.hop_oasis.service;

import com.example.hop_oasis.dto.CartDto;
import com.example.hop_oasis.dto.CartItemDto;
import com.example.hop_oasis.dto.ItemRequestDto;
import com.example.hop_oasis.dto.ItemRequestDtoWithMeasure;
import com.example.hop_oasis.model.ItemType;

import java.util.List;

public interface CartService {

    CartDto getAllItemsByCartId(Long cartId);

    CartItemDto create(ItemRequestDto itemRequestDto);

    void removeItem(Long cartId, Long itemId, ItemType itemType, double measureValue);

    void delete(Long cartId);

    CartDto updateCart(Long cartId, List<ItemRequestDtoWithMeasure> items);
}