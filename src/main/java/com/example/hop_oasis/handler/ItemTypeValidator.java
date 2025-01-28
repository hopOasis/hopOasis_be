package com.example.hop_oasis.handler;

import com.example.hop_oasis.model.ItemType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ItemTypeValidator implements ConstraintValidator<ValidItemType, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        for (ItemType itemType : ItemType.values()) {
            if (itemType.name().equals(value)) {
                return true;
            }
        }
        return false;
    }
}
