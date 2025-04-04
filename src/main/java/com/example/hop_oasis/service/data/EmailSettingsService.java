package com.example.hop_oasis.service.data;

import com.example.hop_oasis.model.EmailSettings;
import com.example.hop_oasis.repository.EmailSettingsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailSettingsService {
    private final EmailSettingsRepository settingsRepository;

    public boolean isEmailNotificationsEnabled() {
        return settingsRepository.findById(1L)
                .map(EmailSettings::isEmailNotificationsEnabled)
                .orElse(true);
    }

    public void updateEmailNotifications(boolean enabled) {
        EmailSettings settings = settingsRepository.findById(1L)
                .orElse(new EmailSettings());
        settings.setEmailNotificationsEnabled(enabled);
        settingsRepository.save(settings);
    }
}
