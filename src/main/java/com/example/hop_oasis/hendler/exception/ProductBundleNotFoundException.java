package com.example.hop_oasis.hendler.exception;

import static java.lang.String.format;

public class ProductBundleNotFoundException extends RuntimeException{
    public ProductBundleNotFoundException(String message, Object o){
        super(format(message,o));
    }
}
