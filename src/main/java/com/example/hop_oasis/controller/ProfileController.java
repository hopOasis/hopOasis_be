package com.example.hop_oasis.controller;

import com.example.hop_oasis.dto.ProfileInfoDto;
import com.example.hop_oasis.model.User;
import com.example.hop_oasis.service.data.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/profile")
public class ProfileController {
    private final ProfileService profileService;

    @GetMapping
    public ResponseEntity<ProfileInfoDto> getProfile(@AuthenticationPrincipal User user) {
        ProfileInfoDto profileInfoDto = profileService.getProfileByUserId(user.getId());
        return ResponseEntity.ok().body(profileInfoDto);
    }
}
