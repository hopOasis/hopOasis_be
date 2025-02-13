package com.example.hop_oasis.controller;

import com.example.hop_oasis.enums.*;
import com.example.hop_oasis.model.ItemType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/enums")
public class EnumController {
    @GetMapping("/item-types")
    public ResponseEntity<List<String>> getItemTypes() {
        List<String> itemTypes = Arrays.stream(ItemType.values())
                .map(Enum::name)
                .toList();
        return ResponseEntity.ok(itemTypes);
    }

    @GetMapping("/delivery-methods")
    public ResponseEntity<List<String>> getDeliveryMethods() {
        List<String> deliveryMethods = Arrays.stream(DeliveryMethod.values())
                .map(Enum::name)
                .toList();
        return ResponseEntity.ok(deliveryMethods);
    }

    @GetMapping("/delivery-statuses")
    public ResponseEntity<List<String>> getDeliveryStatuses() {
        List<String> deliveryStatuses = Arrays.stream(DeliveryStatus.values())
                .map(Enum::name)
                .toList();
        return ResponseEntity.ok(deliveryStatuses);
    }

    @GetMapping("/delivery-types")
    public ResponseEntity<List<String>> getDeliveryTypes() {
        List<String> deliveryTypes = Arrays.stream(DeliveryType.values())
                .map(Enum::name)
                .toList();
        return ResponseEntity.ok(deliveryTypes);
    }

    @GetMapping("/payment-types")
    public ResponseEntity<List<String>> getPaymentTypes() {
        List<String> paymentTypes = Arrays.stream(PaymentType.values())
                .map(Enum::name)
                .toList();
        return ResponseEntity.ok(paymentTypes);
    }

    @GetMapping("/roles")
    public ResponseEntity<List<String>> getRoles() {
        List<String> roles = Arrays.stream(Role.values())
                .map(Enum::name)
                .toList();
        return ResponseEntity.ok(roles);
    }
}
