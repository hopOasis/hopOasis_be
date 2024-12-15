package com.example.hop_oasis.service.data;

import com.example.hop_oasis.dto.GoogleUserInfoResponse;
import com.example.hop_oasis.dto.UserForAdminResponse;
import com.example.hop_oasis.dto.UserResponse;
import com.example.hop_oasis.enums.Role;
import com.example.hop_oasis.model.User;
import com.example.hop_oasis.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserAuthenticated userAuthenticated;

    @Value("${spring.security.oauth2.client.registration.google.clientId}")
    private String googleClientId;
    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String redirectUri;
    @Value("${spring.security.response-type}")
    private String responseType;
    @Value("${spring.security.scope}")
    private String scope;
    @Value("${spring.security.state}")
    private String state;
    @Value("${spring.security.access-type}")
    private String accessType;
    @Value("${spring.security.google-oauth-url}")
    private String accountGoogleOauthUrl;

    public UserResponse getUser(Long userId) {
        User authenticatedUser = userAuthenticated.getAuthenticatedUser();
        if (authenticatedUser.getId().equals(userId) || authenticatedUser.getRole().equals(Role.ADMIN)) {
            return userRepository.findUserById(userId).orElseThrow();
        } else {
            throw new RuntimeException("User not found");
        }

    }

    public List<UserForAdminResponse> getAllUsers() {
        User authenticatedUser = userAuthenticated.getAuthenticatedUser();
        if (authenticatedUser.getRole().equals(Role.ADMIN)) {
            return userRepository.findAllUsers().orElseThrow();
        } else {
            throw new RuntimeException("User not found");
        }

    }

    public UserResponse updateUser(Long userId, UserResponse user) {
        User authenticatedUser = userAuthenticated.getAuthenticatedUser();
        User curentUser = userRepository.findById(userId).orElseThrow();
        if (authenticatedUser.getEmail().equals(curentUser.getEmail())) {
            curentUser.setEmail(user.getEmail());
            curentUser.setFirstName(user.getFirstName());
            curentUser.setLastName(user.getLastName());
            userRepository.save(curentUser);
            return UserResponse.builder()
                    .email(curentUser.getEmail())
                    .firstName(curentUser.getFirstName())
                    .lastName(curentUser.getLastName())
                    .build();
        } else {
            throw new RuntimeException("User not found");
        }

    }

    public String deleteUser(Long userId) {
        User authenticatedUser = userAuthenticated.getAuthenticatedUser();
        if (userId.equals(authenticatedUser.getId()) || authenticatedUser.getRole().equals(Role.ADMIN)) {
            userRepository.deleteById(userId);
            return "User deleted";
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public String getGoogleUrl() {
        return String.format(accountGoogleOauthUrl,
                googleClientId,
                redirectUri,
                responseType,
                scope,
                state,
                accessType);
    }

    public User getOrCreateUserFromGoogle(GoogleUserInfoResponse googleUserInfoResponse) {
        Optional<User> existingUser = userRepository.findByEmail(googleUserInfoResponse.getEmail());

        if (existingUser.isPresent()) {
            return existingUser.get();
        }

        User newUser = new User();
        newUser.setFirstName(googleUserInfoResponse.getName());
        newUser.setLastName("");
        newUser.setEmail(googleUserInfoResponse.getEmail());
        newUser.setPassword("GOOGLE");
        newUser.setRole(Role.USER);

        return userRepository.save(newUser);
    }

}

