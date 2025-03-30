package com.example.hop_oasis.repository;

import com.example.hop_oasis.model.EmailSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailSettingsRepository extends JpaRepository<EmailSettings, Long> {
    Optional<EmailSettings> findById(Long id);
}
