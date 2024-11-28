package com.example.hop_oasis.dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginRequest {

    private String email;
    private String password;
}
