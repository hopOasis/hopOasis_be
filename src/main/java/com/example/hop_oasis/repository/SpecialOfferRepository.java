package com.example.hop_oasis.repository;

import com.example.hop_oasis.model.SpecialOfferProduct;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SpecialOfferRepository extends JpaRepository<SpecialOfferProduct, Long> {

}
