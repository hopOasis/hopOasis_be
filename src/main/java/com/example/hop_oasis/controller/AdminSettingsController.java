package com.example.hop_oasis.controller;

import com.example.hop_oasis.service.data.EmailSettingsService;
import com.example.hop_oasis.utils.EmailPattern;
import com.example.hop_oasis.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("admin/settings")
public class AdminSettingsController {
    private final EmailSettingsService settingsService;

    @PutMapping("/email-notifications")
    public ResponseEntity<Map<String, Object>> updateEmailNotifications(
            @RequestBody Map<String, Boolean> request) {
        boolean enabled = request.getOrDefault("enabled", true);
        settingsService.updateEmailNotifications(enabled);
        return ApiResponse
                .success(enabled ? EmailPattern.ENABLED_NOTIFICATIONS : EmailPattern.DISABLED_NOTIFICATIONS);

    }
}
