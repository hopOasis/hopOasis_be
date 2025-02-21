package com.example.hop_oasis.dto;

import com.example.hop_oasis.enums.DeliveryMethod;
import com.example.hop_oasis.enums.DeliveryStatus;
import com.example.hop_oasis.enums.DeliveryType;
import com.example.hop_oasis.enums.PaymentType;
import com.example.hop_oasis.utils.OnDeliveryValidation;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderRequestDto {
    @Pattern(regexp = "^\\+380\\d{9}$", message = "Phone number must be in format +380xxxxxxxxx")
    private String customerPhoneNumber;
    @NotNull(message = "Payment type is required")
    private PaymentType paymentType;
    @NotNull(message = "Delivery type is required")
    private DeliveryType deliveryType;
    @NotNull(groups = OnDeliveryValidation.class, message = "Delivery method is required")
    private DeliveryMethod deliveryMethod;
    @NotBlank(groups = OnDeliveryValidation.class, message = "Delivery address is required")
    private String deliveryAddress;
    private DeliveryStatus deliveryStatus;
}
