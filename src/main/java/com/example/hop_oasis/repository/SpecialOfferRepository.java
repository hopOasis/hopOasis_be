package com.example.hop_oasis.repository;

import com.example.hop_oasis.model.SpecialOfferProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpecialOfferRepository extends JpaRepository<SpecialOfferProduct, Long> {
}
