package com.example.hop_oasis.hendler.exception;

import static java.lang.String.format;

public class ImageNotFoundException extends RuntimeException{
    public ImageNotFoundException(String message,Object o) {
        super(format(message,o));

    }
}
