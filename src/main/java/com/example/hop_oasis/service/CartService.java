package com.example.hop_oasis.service;

import com.example.hop_oasis.dto.CartDto;

public interface CartService {
    CartDto getAllItems();

    void add(Long beerId);

    void updateQuantity(Long beerId, int quantity);

    void delete();
}
