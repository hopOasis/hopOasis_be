package com.example.hop_oasis.repository;

import com.example.hop_oasis.model.CartItem;
import com.example.hop_oasis.model.ItemType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

  List<CartItem> findByCartId(Long cartId);

  void deleteByCartId(Long cartId);

  List<CartItem> findByCartIdAndItemIdAndItemType(Long cartId, Long itemId, ItemType itemType);
}
