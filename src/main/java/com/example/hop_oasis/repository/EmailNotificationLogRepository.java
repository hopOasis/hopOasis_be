package com.example.hop_oasis.repository;

import com.example.hop_oasis.model.EmailNotificationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailNotificationLogRepository extends JpaRepository<EmailNotificationLog, Long> {
}
