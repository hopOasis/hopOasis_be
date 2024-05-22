package com.example.hop_oasis.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileDto {
    private Long id;
    @NotBlank(message = "Будь ласка, заповніть поле")
    private String firstName;
    @NotBlank(message = "Будь ласка, заповніть поле")
    private String lastName;
    @NotBlank(message = "Будь ласка, заповніть поле")
    private String address;
    @NotBlank(message = "Будь ласка, заповніть поле")
    private String phoneNumber;
}
