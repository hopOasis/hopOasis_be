package com.example.hop_oasis.service.sheduler;

import com.example.hop_oasis.service.data.CartServiceImpl;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartCleanupService {
    private final CartServiceImpl cartService;

    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void clearCart(){
        cartService.delete();
    }
}
