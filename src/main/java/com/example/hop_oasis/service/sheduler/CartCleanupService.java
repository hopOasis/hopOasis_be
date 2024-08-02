package com.example.hop_oasis.service.sheduler;

import com.example.hop_oasis.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class CartCleanupService {
    private final CartRepository cartRepository;

    @Scheduled(cron = "0 0 0 * * ?")
    public void deleteOldCarts() {
        LocalDateTime oneDayAgo = LocalDateTime.now().minusDays(1);
        Date oneDayAgoDate = Date.from(oneDayAgo.atZone(ZoneId.systemDefault()).toInstant());
        cartRepository.deleteByCreatedAtBefore(oneDayAgoDate);
    }
}
