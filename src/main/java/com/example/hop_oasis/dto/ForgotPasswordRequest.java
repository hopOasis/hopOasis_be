package com.example.hop_oasis.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ForgotPasswordRequest {
   @Email
   @NotBlank
   private String email;
}
