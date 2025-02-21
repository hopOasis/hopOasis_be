package com.example.hop_oasis.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum DeliveryMethod {
    COURIER("Courier"),
    POST_OFFICE("Post office"),
    PARCEL_TERMINAL("Parcel terminal");

    private final String method;

    DeliveryMethod(String method) {
        this.method = method;
    }

    @JsonValue
    public String getMethod() {
        return method;
    }

    @JsonCreator
    public static DeliveryMethod fromString(String value) {
        return DeliveryMethod.valueOf(value.toUpperCase());
    }
}
