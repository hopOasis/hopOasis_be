package com.example.hop_oasis.repository;

import com.example.hop_oasis.model.Snack;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SnackRepository extends JpaRepository<Snack, Long> {

    Page<Snack> findAll(Pageable pageable);
}
