package com.example.hop_oasis.hendler.exception;

import static java.lang.String.format;

public class BeerNotFoundException extends RuntimeException{
    public BeerNotFoundException(String message,Object o) {
        super(format(message,o));

    }
}
