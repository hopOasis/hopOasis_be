package com.example.hop_oasis.service.data;

import com.example.hop_oasis.dto.TokenResponse;
import com.example.hop_oasis.dto.UserLoginRequest;
import com.example.hop_oasis.dto.UserRegisterRequest;
import com.example.hop_oasis.enums.Role;
import com.example.hop_oasis.handler.exception.ResourceNotFoundException;
import com.example.hop_oasis.handler.exception.UnauthorizedException;
import com.example.hop_oasis.model.User;
import com.example.hop_oasis.repository.UserRepository;
import com.example.hop_oasis.utils.EmailValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
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
    private final EmailValidator emailValidator;

    public TokenResponse register(UserRegisterRequest registerRequest) {
        User user = User.builder()
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .email(emailValidator.validateExistingEmail(registerRequest.getEmail()))
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
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );
        } catch (BadCredentialsException ex) {
            throw new UnauthorizedException("Invalid email or password");
        }

        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new UnauthorizedException("User not found"));

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

}
