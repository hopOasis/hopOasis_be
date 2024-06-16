package com.example.hop_oasis.hendler.exception;

import static java.lang.String.format;

public class SnackNotFoundException extends RuntimeException{
    public SnackNotFoundException(String message, Object o){
        super(format(message,o));
    }

}
