package com.example.hop_oasis.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public class TokenResponse {
    @JsonProperty("access_token")
    private String accessToken;
}
