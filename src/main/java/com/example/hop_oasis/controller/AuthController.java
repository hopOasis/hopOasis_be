package com.example.hop_oasis.controller;

import com.example.hop_oasis.dto.*;
import com.example.hop_oasis.model.User;
import com.example.hop_oasis.service.data.AuthenticationService;
import com.example.hop_oasis.service.data.DefaultOauthServiceImpl;
import com.example.hop_oasis.service.data.JwtService;
import com.example.hop_oasis.service.data.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final JwtService jwtService;
    private final DefaultOauthServiceImpl oauthService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody UserLoginRequest loginRequest) {
        return ResponseEntity.ok(authenticationService.authenticate(loginRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<TokenResponse> register(@Valid @RequestBody UserRegisterRequest registerRequest) {
        return ResponseEntity.ok(authenticationService.register(registerRequest));
    }

    @GetMapping("/refresh/token")
    public ResponseEntity<TokenResponse> refreshToken() {
        return ResponseEntity.ok(authenticationService.refreshToken());
    }

    @PostMapping("/google/login")
    public ResponseEntity<TokenResponse> googleLogin(@RequestBody GoogleAuthRequest request) {
        GoogleTokenResponse googleToken = oauthService.exchangeCodeForToken(request.getCode());
        GoogleUserInfoResponse userInfo = oauthService.getUserInfo(googleToken.getAccessToken());

        User user = userService.getOrCreateUserFromGoogle(userInfo);

        String accessToken = jwtService.generateToken(user);

        return ResponseEntity.ok(TokenResponse.builder()
                .accessToken(accessToken)
                .build());
    }

    @GetMapping("/google/url")
    public String getGoogleAuthUrl() {
        return userService.getGoogleUrl();
    }

}
