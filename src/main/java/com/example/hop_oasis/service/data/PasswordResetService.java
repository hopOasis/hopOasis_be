package com.example.hop_oasis.service.data;

import com.example.hop_oasis.handler.exception.ResourceNotFoundException;
import com.example.hop_oasis.model.PasswordResetToken;
import com.example.hop_oasis.model.User;
import com.example.hop_oasis.repository.PasswordResetTokenRepository;
import com.example.hop_oasis.repository.UserRepository;
import com.example.hop_oasis.utils.EmailPattern;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetService {
    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void sendResetPasswordEmail(String email) {
        userRepository.findByEmail(email).ifPresent(user -> {
            tokenRepository.deleteByUser(user);
            String token = UUID.randomUUID().toString();
            LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(30);
            PasswordResetToken resetToken = PasswordResetToken.builder()
                    .token(token)
                    .user(user)
                    .expiryDate(expiryDate)
                    .build();
            tokenRepository.save(resetToken);
            String resetLink = "http://localhost:4200/reset-password?token=" + token;
            String emailContent = EmailPattern.buildMessageForResetPassword(resetLink);
            emailService.sendEmail(user.getEmail(), "Відновлення паролю", emailContent);
        });
    }

    @Transactional
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid token", ""));
        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Token expired");
        }
        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        tokenRepository.delete(resetToken);
    }
}
