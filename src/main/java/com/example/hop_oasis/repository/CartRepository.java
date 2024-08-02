package com.example.hop_oasis.repository;

import com.example.hop_oasis.model.Cart;
import java.util.Date;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

  void deleteByCreatedAtBefore(Date date);

}
