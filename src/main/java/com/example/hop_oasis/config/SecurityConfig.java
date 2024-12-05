package com.example.hop_oasis.config;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

import com.example.hop_oasis.enums.Role;
import com.example.hop_oasis.filter.JwtAuthenticationFilter;
import com.example.hop_oasis.service.data.UserDetailsServiceImpl;
import jakarta.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @PostConstruct
    public void init() {
        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowCredentials(false);
        configuration.setAllowedHeaders(Collections.singletonList("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .cors(Customizer.withDefaults())
            .csrf(AbstractHttpConfigurer::disable)
            .anonymous(AbstractHttpConfigurer::disable)
            .authorizeRequests()
            .requestMatchers(antMatcher("/auth/register"),
                             antMatcher("/auth/login"),
                             antMatcher("/images/**"),
                             antMatcher(GET, "/beers"),
                             antMatcher(GET, "/beers/{id}"),
                             antMatcher(GET, "/ciders"),
                             antMatcher(GET, "/ciders/{id}"),
                             antMatcher(GET, "/products-bundle"),
                             antMatcher(GET, "/products-bundle/{id}"),
                             antMatcher(GET, "/snacks"),
                             antMatcher(GET, "/snacks/{id}"),
                             antMatcher(GET, "/special-offers/active"),
                             antMatcher(GET, "/carts/{cartId}"),
                             antMatcher(POST, "/beers/{id}/ratings"),
                             antMatcher(POST, "/ciders/{id}/ratings"),
                             antMatcher(POST, "/products-bundle/{id}/ratings"),
                             antMatcher(POST, "/snacks/{id}/ratings"),
                             antMatcher(POST, "/carts"),
                             antMatcher(PUT, "/carts"),
                             antMatcher(DELETE, "/carts/remove/{cartId}")).permitAll()

            .requestMatchers(antMatcher("/special-offers/**"),
                             antMatcher(GET, "/users"),
                             antMatcher(POST, "/beers"),
                             antMatcher(POST, "/ciders"),
                             antMatcher(POST, "/products-bundle"),
                             antMatcher(POST, "/snacks"),
                             antMatcher(POST, "/special-offers"),
                             antMatcher(POST, "/beers/{beerId}/images"),
                             antMatcher(POST, "/ciders/{ciderId}/images"),
                             antMatcher(POST, "/products-bundle/{id}/images"),
                             antMatcher(POST, "/snacks/{snackId}/images"),
                             antMatcher(PUT, "/beers/{id}"),
                             antMatcher(PUT, "/ciders/{id}"),
                             antMatcher(PUT, "/products-bundle/{id}"),
                             antMatcher(PUT, "/snacks/{id}"),
                             antMatcher(DELETE, "/beers/{id}"),
                             antMatcher(DELETE, "/beers//images"),
                             antMatcher(DELETE, "/ciders/{id}"),
                             antMatcher(DELETE, "/ciders/images"),
                             antMatcher(DELETE, "/products-bundle/{id}"),
                             antMatcher(DELETE, "/products-bundle/images"),
                             antMatcher(DELETE, "/snacks/{id}"),
                             antMatcher(DELETE, "/snacks/images")).hasAuthority(Role.ADMIN.name())
            .and()
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
