package com.example.hop_oasis.service.data;

import com.example.hop_oasis.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserAuthenticated {

    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() &&
                authentication.getPrincipal() instanceof User) {
            User currentUser = (User) authentication.getPrincipal();

            if (currentUser != null) {
                return currentUser;
            } else {
                throw new RuntimeException("User not found");
            }
        }
        return null;
    }
}
