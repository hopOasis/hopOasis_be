package com.example.hop_oasis.repository;

import com.example.hop_oasis.dto.ReviewInfoDto;
import com.example.hop_oasis.model.ItemType;
import com.example.hop_oasis.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByItemIdAndItemType(Long itemId, ItemType itemType);
}
