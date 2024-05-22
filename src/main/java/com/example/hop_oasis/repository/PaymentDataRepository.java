package com.example.hop_oasis.repository;

import com.example.hop_oasis.model.PaymentData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentDataRepository extends JpaRepository<PaymentData, Long> {

}
