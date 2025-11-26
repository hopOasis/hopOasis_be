package com.example.hop_oasis.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenResponse {
    private Long userId;
    @JsonProperty("access_token")
    private String accessToken;
}
