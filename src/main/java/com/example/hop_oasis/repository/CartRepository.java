package com.example.hop_oasis.repository;

import com.example.hop_oasis.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    void deleteByCreatedAtBefore(Date date);

}
