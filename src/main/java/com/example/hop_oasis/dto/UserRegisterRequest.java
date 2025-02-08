package com.example.hop_oasis.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserRegisterRequest {
    @Pattern(
            regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
            message = "Invalid email format"
    )
    private String email;
    private String password;
    @NotBlank(message = "First name can not be empty")
    @Pattern(regexp = "^[A-Za-zА-Яа-яЁёІіЇїЄєҐґ]+(?: [A-Za-zА-Яа-яЁёІіЇїЄєҐґ]+)*$",
            message = "First name must contain only letters and spaces")
    private String firstName;
    @NotBlank(message = "Last name can not be empty")
    @Pattern(regexp = "^[A-Za-zА-Яа-яЁёІіЇїЄєҐґ]+(?: [A-Za-zА-Яа-яЁёІіЇїЄєҐґ]+)*$",
            message = "Last name must contain only letters and spaces")
    private String lastName;
}
