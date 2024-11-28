package com.example.hop_oasis.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserRegisterRequest {

    private String email;
    private String password;
    private String firstName;
    private String lastName;
}
