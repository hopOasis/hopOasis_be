package com.example.hop_oasis.repository;

import com.example.hop_oasis.model.Cider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CiderRepository extends JpaRepository<Cider, Long>, JpaSpecificationExecutor<Cider> {

}
