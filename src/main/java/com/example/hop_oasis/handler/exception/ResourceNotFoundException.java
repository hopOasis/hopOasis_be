package com.example.hop_oasis.handler.exception;

import static java.lang.String.format;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message, Object o) {
        super(format(message, o));
    }
}
