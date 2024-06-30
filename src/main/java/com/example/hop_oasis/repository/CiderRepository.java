package com.example.hop_oasis.repository;

import com.example.hop_oasis.model.Cider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CiderRepository extends JpaRepository<Cider, Long> {
    Page<Cider> findAll(Pageable pageable);
}
