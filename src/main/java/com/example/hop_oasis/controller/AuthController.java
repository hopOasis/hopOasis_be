package com.example.hop_oasis.controller;

import com.example.hop_oasis.dto.TokenResponse;
import com.example.hop_oasis.dto.UserLoginRequest;
import com.example.hop_oasis.dto.UserRegisterRequest;
import com.example.hop_oasis.service.data.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody UserLoginRequest loginRequest) {
        return ResponseEntity.ok(authenticationService.authenticate(loginRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<TokenResponse> register(@RequestBody UserRegisterRequest registerRequest) {
        return ResponseEntity.ok(authenticationService.register(registerRequest));
    }

    @GetMapping("/refresh/token")
    public ResponseEntity<TokenResponse> refreshToken() {
        return ResponseEntity.ok(authenticationService.refreshToken());
    }

}
