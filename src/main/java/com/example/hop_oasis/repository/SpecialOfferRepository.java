package com.example.hop_oasis.repository;


import com.example.hop_oasis.model.SpecialOfferProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;


public interface SpecialOfferRepository extends JpaRepository<SpecialOfferProduct, Long> {
    @Query("SELECT s.id FROM SpecialOfferProduct s")
    List<Long> findAllIds();

    @Query("SELECT s.id FROM SpecialOfferProduct s WHERE s.active = true")
    Long findActiveSpecialOfferProductIds();
}

