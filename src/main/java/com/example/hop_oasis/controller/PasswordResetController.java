package com.example.hop_oasis.controller;

import com.example.hop_oasis.dto.ForgotPasswordRequest;
import com.example.hop_oasis.dto.ResetPasswordRequest;
import com.example.hop_oasis.service.data.PasswordResetService;
import com.example.hop_oasis.utils.EmailPattern;
import com.example.hop_oasis.utils.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/password")
public class PasswordResetController {
    private final PasswordResetService passwordResetService;

    @PostMapping("/forgot")
    public ResponseEntity<Map<String, Object>> forgotPassword(@Valid @RequestBody ForgotPasswordRequest passwordRequest) {
        passwordResetService.sendResetPasswordEmail(passwordRequest.getEmail());
        return ApiResponse.success(EmailPattern.VALID_EMAIL_RESPONSE);
    }

    @PostMapping("/reset")
    public ResponseEntity<Map<String, Object>> resetPassword(@Valid @RequestBody ResetPasswordRequest passwordRequest) {
        passwordResetService.resetPassword(passwordRequest.getToken(), passwordRequest.getNewPassword());
        return ApiResponse.success(EmailPattern.SUCCESSFUL_CHANGE_PASSWORD);

    }
}
