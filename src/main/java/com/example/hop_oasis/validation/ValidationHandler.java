package com.example.hop_oasis.validation;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class ValidationHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handlerValidationException(MethodArgumentNotValidException e) {
        Map<String, String> error = new HashMap<>();
        List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
        allErrors.forEach(er -> {
            FieldError field = (FieldError) er;
            error.put(field.getField(), er.getDefaultMessage());
        });
        return error;


    }
}
