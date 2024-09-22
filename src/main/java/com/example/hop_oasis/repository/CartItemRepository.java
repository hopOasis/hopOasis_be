package com.example.hop_oasis.repository;

import com.example.hop_oasis.model.CartItem;
import com.example.hop_oasis.model.ItemType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByCartId(Long cartId);
    void deleteByCartId(Long cartId);
    List<CartItem> findByCartIdAndItemIdAndItemType(Long cartId, Long itemId, ItemType itemType);
    CartItem findByCartIdAndItemIdAndMeasureValueAndItemType(Long cartId, Long itemId, double measureValue, ItemType itemType);
}
