package com.example.hop_oasis.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    @NotBlank(message = "Будь ласка, заповніть поле")
    @Email
    private String email;
    @NotBlank(message = "Будь ласка, заповніть поле")
    private String password;
}
