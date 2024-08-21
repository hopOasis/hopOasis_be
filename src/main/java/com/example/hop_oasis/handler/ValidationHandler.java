package com.example.hop_oasis.handler;

import com.example.hop_oasis.handler.exception.ResourceNotFoundException;
import com.example.hop_oasis.handler.exception.SpecialOfferException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.stream.Collectors;

import static com.example.hop_oasis.handler.ErrorDetails.getResponseEntityErrorMap;

@RestControllerAdvice
public class ValidationHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDetails> handleValidationExceptions(HttpServletRequest request,
                                                                   MethodArgumentNotValidException ex) {
        Map<String, String> errorMap = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        field -> field.getDefaultMessage() != null ? field.getDefaultMessage() : ""
                ));
        return getResponseEntityErrorMap(request.getRequestURI(), errorMap);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleNotFoundExceptions(HttpServletRequest request,
                                                                 Exception ex) {
        return getResponseEntityErrorMap(request.getRequestURI(), makeMapFromException(ex));
    }
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(SpecialOfferException.class)
    public ResponseEntity<?> handleSpecialOfferExceptions() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    private Map<String, String> makeMapFromException(Exception ex) {
        return Map.of(ex.getClass().getSimpleName(), ex.getLocalizedMessage());
    }
}
