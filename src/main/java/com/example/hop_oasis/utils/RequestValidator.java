package com.example.hop_oasis.utils;

import com.example.hop_oasis.handler.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@AllArgsConstructor
public class RequestValidator {
    private static final List<String> ALLOWED_SORT_DIRECTIONS = List.of("asc", "desc");

    public void validateParams(Map<String, String> allParams, String allowedParams) {
        for (String param : allParams.keySet()) {
            if (!allowedParams.contains(param)) {
                throw new ResourceNotFoundException(
                        "Allowed params: " + allowedParams, "");
            }
        }
    }

    public void validateSortDirection(String sortDirection) {
        if (sortDirection != null && !ALLOWED_SORT_DIRECTIONS.contains(sortDirection.toLowerCase())) {
            throw new ResourceNotFoundException("Sort direction must be 'asc' or 'desc'", "");
        }
    }
}
