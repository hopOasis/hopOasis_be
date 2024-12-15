package com.example.hop_oasis.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GoogleUserInfoResponse {

    private String id;
    private String email;
    private String name;
    private String picture;

}
