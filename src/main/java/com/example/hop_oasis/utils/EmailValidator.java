package com.example.hop_oasis.utils;

import com.example.hop_oasis.model.User;
import com.example.hop_oasis.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
@Component
@RequiredArgsConstructor
public class EmailValidator {
    private final UserRepository userRepository;

    public String validateExistingEmail(String newEmail) {
        Optional<User> existingUser = userRepository.findByEmail(newEmail);
        if (existingUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }
        return newEmail;
    }
}
