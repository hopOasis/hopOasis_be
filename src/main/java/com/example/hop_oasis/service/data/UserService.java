package com.example.hop_oasis.service.data;

import com.example.hop_oasis.dto.UserForAdminResponse;
import com.example.hop_oasis.dto.UserResponse;
import com.example.hop_oasis.enums.Role;
import com.example.hop_oasis.model.User;
import com.example.hop_oasis.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserAuthenticated userAuthenticated;

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
}

