package com.example.hop_oasis.controller;

import com.example.hop_oasis.model.AuthenticationResponse;
import com.example.hop_oasis.model.User;
import com.example.hop_oasis.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
          @Valid @RequestBody User request){
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("login")
    public ResponseEntity<AuthenticationResponse> login(
           @Valid @RequestBody User request){
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }



}
