package com.example.hop_oasis.dto;

import lombok.*;
import org.hibernate.validator.constraints.CreditCardNumber;
import jakarta.validation.constraints.*;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PaymentDataDto {
    private Long id;
    @CreditCardNumber(message = "Неправильний формат номеру карти")
    private int cardNumber;
    @Digits(integer = 3, fraction = 0, message = "Невірний формат")
    private int cvv;
    @Pattern(regexp = "^(0[1-9]|1[0-2])([\\/])([2-9][0-9])$",
            message = "Повинен бути формат MM/YY")
    private String expiryDate;
    private UserProfileDto userProfileDto;
}
