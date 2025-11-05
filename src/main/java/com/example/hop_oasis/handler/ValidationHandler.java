package com.example.hop_oasis.handler;

import com.example.hop_oasis.handler.exception.ResourceNotFoundException;
import com.example.hop_oasis.handler.exception.SpecialOfferException;
import com.example.hop_oasis.handler.exception.UnauthorizedException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.hop_oasis.handler.ErrorDetails.getResponseEntityErrorMap;

@RestControllerAdvice
public class ValidationHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDetails> handleValidationExceptions(HttpServletRequest request,
                                                                   MethodArgumentNotValidException ex) {
        Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage,
                        (existing, replacement) -> existing + ", " + replacement
                ));

        return ErrorDetails.getResponseEntityErrorMap(request.getRequestURI(), errors);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(SpecialOfferException.class)
    public ResponseEntity<?> handleSpecialOfferExceptions() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    private Map<String, String> makeMapFromException(Exception ex) {
        return Map.of(ex.getClass().getSimpleName(), ex.getLocalizedMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorDetails> handleConstraintViolationException(
            ConstraintViolationException ex, WebRequest request) {

        Map<String, String> errors = new HashMap<>();

        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.put(violation.getPropertyPath().toString(), violation.getMessage());
        }

        String path = ((ServletWebRequest) request).getRequest().getRequestURI();

        return ErrorDetails.getResponseEntityErrorMap(path, errors);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorDetails> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException ex, WebRequest request) {

        Map<String, String> errors = new HashMap<>();
        String paramName = ex.getName();
        String expectedType = ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown";

        errors.put(paramName, "Must be of type " + expectedType);

        String path = ((ServletWebRequest) request).getRequest().getRequestURI();

        return ErrorDetails.getResponseEntityErrorMap(path, errors);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorDetails> handleIllegalArgumentException(HttpServletRequest request, IllegalArgumentException ex) {
        Map<String, String> errors = Collections.singletonMap("error", ex.getMessage());
        return ErrorDetails.getResponseEntityErrorMap(request.getRequestURI(), errors);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<String> handleUnauthorizedException(UnauthorizedException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<Map<String, String>> handleInvalidEnumValue(InvalidFormatException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        String fieldName = ex.getPath().get(0).getFieldName();
        String invalidValue = ex.getValue().toString();

        errorResponse.put("error", "Invalid value for field '" + fieldName + "': " + invalidValue);
        errorResponse.put("validValues", "Check enum definition");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> handleResponseStatusException(ResponseStatusException ex) {
        return ResponseEntity.status(ex.getStatusCode()).body(ex.getMessage());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleResourceNotFound(ResourceNotFoundException ex) {
        Map<String, String> error = Map.of("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}

