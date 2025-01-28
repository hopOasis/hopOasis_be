package com.example.hop_oasis.handler;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ItemTypeValidator.class)
public @interface ValidItemType {
    String message() default "Invalid item type";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
