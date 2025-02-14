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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

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
                .authorizeHttpRequests(r -> r
                        .requestMatchers("/auth/**",
                                "/assets/icons/**",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/swagger-ui/**",
                                "/api-docs/**")

                        .permitAll()
                        .requestMatchers(HttpMethod.POST, "/beers", "/beers/{beerId}/images", "/ciders",
                                "/ciders/{ciderId}/images", "/products-bundle", "/products-bundle/{id}/images",
                                "/snacks", "/snacks/{snackId}/images", "/special-offers"
                        ).hasAuthority(Role.ADMIN.name())

                        .requestMatchers(HttpMethod.PUT, "/beers/{id}", "/ciders/{id}", "/products-bundle/{id}",
                                "/snacks/{id}", "/special-offers/{offerId}", "/special-offers/name/{offerId}"
                        ).hasAuthority(Role.ADMIN.name())

                        .requestMatchers(HttpMethod.DELETE, "/beers/{id}", "/beers/images", "/ciders/{id}",
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
                        .requestMatchers(HttpMethod.GET, "/all-products").permitAll()

                        .requestMatchers(HttpMethod.PUT, "/orders/{orderId}").permitAll()

                        .requestMatchers(HttpMethod.GET, "/enums/item-types").permitAll()
                        .requestMatchers(HttpMethod.GET, "/enums/delivery-methods").permitAll()
                        .requestMatchers(HttpMethod.GET, "/enums/delivery-statuses").permitAll()
                        .requestMatchers(HttpMethod.GET, "/enums/delivery-types").permitAll()
                        .requestMatchers(HttpMethod.GET, "/enums/payment-types").permitAll()
                        .requestMatchers(HttpMethod.GET, "/enums/roles").permitAll()


                        .requestMatchers(HttpMethod.PUT, "/carts").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/carts/remove/{cartId}").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/carts/clear/{cartId}").permitAll()


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
