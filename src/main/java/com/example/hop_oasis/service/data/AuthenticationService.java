package com.example.hop_oasis.service.data;

import com.example.hop_oasis.dto.TokenResponse;
import com.example.hop_oasis.dto.UserLoginRequest;
import com.example.hop_oasis.dto.UserRegisterRequest;
import com.example.hop_oasis.enums.Role;
import com.example.hop_oasis.handler.exception.ResourceNotFoundException;
import com.example.hop_oasis.model.User;
import com.example.hop_oasis.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserAuthenticated userAuthenticated;

    public TokenResponse register(UserRegisterRequest registerRequest) {
        User user = User.builder()
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(Role.USER)
                .build();
        user = userRepository.save(user);
        String jwtToken = jwtService.generateToken(user);


        return TokenResponse.builder()
                .accessToken(jwtToken)
                .build();

    }

    public TokenResponse authenticate(UserLoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );
        User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(() ->
                new RuntimeException("User not found"));
        String token = jwtService.generateToken(user);

        return TokenResponse.builder()
                .accessToken(token)
                .build();

    }

    public TokenResponse refreshToken() {
        User user = userAuthenticated.getAuthenticatedUser();

        String newAccessToken = jwtService.generateToken(user);
        return TokenResponse.builder()
                .accessToken(newAccessToken)
                .build();

    }

    public boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            User user = userRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found", ""));
            return user.getRole() == Role.ADMIN;
        }
        return false;
    }

}
