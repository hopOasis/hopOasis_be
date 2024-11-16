package com.example.hop_oasis.config;

import com.example.hop_oasis.enums.Role;
import com.example.hop_oasis.filter.JwtAuthenticationFilter;
import com.example.hop_oasis.service.data.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(r -> r
                        .requestMatchers("/auth/register", "/auth/login",
                                "/assets/icons/**",
                                "/css/**",
                                "/js/**",
                                "/images/**")

                        .permitAll()
                        .requestMatchers(HttpMethod.POST, "/beers", "/beers/{beerId}/images", "/ciders",
                                "/ciders/{ciderId}/images", "/products-bundle", "/products-bundle/{id}/images",
                                "/snacks", "/snacks/{snackId}/images", "/special-offers"
                        ).hasAuthority(Role.ADMIN.name())

                        .requestMatchers(HttpMethod.PUT, "/beers/{id}", "/ciders/{id}", "/products-bundle/{id}",
                                "/snacks/{id}", "/special-offers/{offerId}", "/special-offers/name/{offerId}"
                        ).hasAuthority(Role.ADMIN.name())

                        .requestMatchers(HttpMethod.DELETE, "/beers/{id}", "/beers//images", "/ciders/{id}",
                                "/ciders/images", "/products-bundle/{id}", "/products-bundle/images",
                                "/snacks/{id}", "/snacks/images", "/special-offers/{offerId}",
                                "/special-offers/{offerId}/beers/{beerId}",
                                "/special-offers/{offerId}/ciders/{ciderId}",
                                "/special-offers/{offerId}/snacks/{snackId}",
                                "/special-offers/{offerId}/products-bundle/{productBundleId}"
                        ).hasAuthority(Role.ADMIN.name())


                        .requestMatchers(HttpMethod.GET, "/special-offers/{offerId}/beers/{beerId}",
                                "/special-offers/{offerId}/ciders/{ciderId}",
                                "/special-offers/{offerId}/snacks/{snackId}",
                                "/special-offers/{offerId}/products-bundle/{productBundleId}",
                                "/special-offers/{offerId}", "/special-offers", "/users"
                        ).hasAuthority(Role.ADMIN.name())

                        .requestMatchers(HttpMethod.GET, "/users/{userId}").hasAnyAuthority(Role.ADMIN.name(), Role.USER.name())
                        .requestMatchers(HttpMethod.PUT, "/users/{userId}").hasAuthority(Role.USER.name())
                        .requestMatchers(HttpMethod.DELETE, "/users/{userId}").hasAnyAuthority(Role.ADMIN.name(), Role.USER.name())

                        .requestMatchers(HttpMethod.GET, "/beers", "/beers/{id}", "/ciders", "/ciders/{id}",
                                "/products-bundle", "/products-bundle/{id}", "/snacks", "/snacks/{id}",
                                "/special-offers/active", "/carts/{cartId}"
                        ).permitAll()

                        .requestMatchers(HttpMethod.POST, "/beers/{id}/ratings", "/ciders/{id}/ratings",
                                "/products-bundle/{id}/ratings", "/snacks/{id}/ratings", "/carts"
                        ).permitAll()

                        .requestMatchers(HttpMethod.PUT, "/carts").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/carts/remove/{cartId}").permitAll()


                        .anyRequest()
                        .authenticated())
                .userDetailsService(userDetailsService)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
            throws Exception {
        return configuration.getAuthenticationManager();
    }
}
