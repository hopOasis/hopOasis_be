package com.example.hop_oasis.utils;

import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Map;

public class ApiResponse {
    public static ResponseEntity<Map<String, Object>> success(String message) {
        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", message
        ));
    }

    public static ResponseEntity<Map<String, Object>> orderEmailResponse(Long orderId,
                                                                         boolean emailSent) {
        return ResponseEntity.ok(Map.of(
                "orderId", orderId,
                "emailSent", emailSent,
                "sentAt", LocalDateTime.now()
        ));
    }

}
