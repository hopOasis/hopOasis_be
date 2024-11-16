package com.example.hop_oasis.repository;

import com.example.hop_oasis.model.Snack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SnackRepository extends JpaRepository<Snack, Long>, JpaSpecificationExecutor<Snack> {

}
