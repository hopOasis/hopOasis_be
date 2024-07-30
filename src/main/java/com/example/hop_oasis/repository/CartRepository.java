package com.example.hop_oasis.repository;

import com.example.hop_oasis.model.Cart;
import com.example.hop_oasis.model.ItemType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByItemIdAndItemType(Long itemId, ItemType itemType);

    List<Cart> findByItemType(ItemType itemType);
}
