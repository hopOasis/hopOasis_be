package com.example.hop_oasis.dto;

import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserResponse {
    private String email;
    private String firstName;
    private String lastName;

    public UserResponse(String email, String firstName, String lastName) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
