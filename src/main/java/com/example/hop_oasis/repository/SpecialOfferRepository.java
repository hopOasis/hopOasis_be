package com.example.hop_oasis.repository;

import com.example.hop_oasis.model.SpecialOfferProduct;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SpecialOfferRepository extends JpaRepository<SpecialOfferProduct, Long> {

  @Query("SELECT s.id FROM SpecialOfferProduct s")
  List<Long> findAllIds();

  @Query("SELECT s.id FROM SpecialOfferProduct s WHERE s.active = true")
  Long findActiveSpecialOfferProductIds();
}

