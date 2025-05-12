package com.example.hop_oasis.utils;

import com.example.hop_oasis.repository.PasswordResetTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class CleanUpExpireToken {
    private final PasswordResetTokenRepository tokenRepository;

    @Scheduled(cron = "0 0 * * * *")
    public void cleanupToken() {
        tokenRepository.deleteAllByExpiryDateBefore(LocalDateTime.now());
    }
}
