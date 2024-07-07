package com.example.hop_oasis.repository;

import com.example.hop_oasis.model.ItemType;
import com.example.hop_oasis.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findByItemIdAndItemType(Long itemId, ItemType itemType);
    int countByItemIdAndItemType(Long itemId, ItemType itemType);
}
