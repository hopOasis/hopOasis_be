package com.example.hop_oasis.dto;

import com.example.hop_oasis.enums.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserForAdminResponse {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private Role role;

    public UserForAdminResponse(Long id, String email, String firstName, String lastName, Role role) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }
}
