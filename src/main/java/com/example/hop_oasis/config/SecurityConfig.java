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
                        .requestMatchers(HttpMethod.POST, "/beers").hasAuthority(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.POST, "/beers/{beerId}/images").hasAuthority(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.PUT, "/beers/{id}").hasAuthority(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.DELETE, "/beers/{id}").hasAuthority(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.DELETE, "/beers//images").hasAuthority(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.POST, "/ciders").hasAuthority(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.POST, "/ciders/{ciderId}/images").hasAuthority(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.PUT, "/ciders/{id}").hasAuthority(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.DELETE, "/ciders/{id}").hasAuthority(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.DELETE, "/ciders/images").hasAuthority(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.POST, "/products-bundle").hasAuthority(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.POST, "/products-bundle/{id}/images").hasAuthority(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.PUT, "/products-bundle/{id}").hasAuthority(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.DELETE, "/products-bundle/{id}").hasAuthority(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.DELETE, "/products-bundle/images").hasAuthority(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.POST, "/snacks").hasAuthority(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.POST, "/snacks/{snackId}/images").hasAuthority(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.PUT, "/snacks/{id}").hasAuthority(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.DELETE, "/snacks/{id}").hasAuthority(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.DELETE, "/snacks/images").hasAuthority(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.PUT, "/special-offers/{offerId}").hasAuthority(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.POST, "/special-offers").hasAuthority(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.PUT, "/special-offers/name/{offerId}").hasAuthority(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.GET, "/special-offers/{offerId}/beers/{beerId}").hasAuthority(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.GET, "/special-offers/{offerId}/ciders/{ciderId}").hasAuthority(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.GET, "/special-offers/{offerId}/snacks/{snackId}").hasAuthority(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.GET, "/special-offers/{offerId}/products-bundle/{productBundleId}").hasAuthority(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.GET, "/special-offers/{offerId}").hasAuthority(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.GET, "/special-offers").hasAuthority(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.DELETE, "/special-offers/{offerId}").hasAuthority(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.DELETE, "/special-offers/{offerId}/beers/{beerId}").hasAuthority(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.DELETE, "/special-offers/{offerId}/ciders/{ciderId}").hasAuthority(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.DELETE, "/special-offers/{offerId}/snacks/{snackId}").hasAuthority(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.DELETE, "/special-offers/{offerId}/products-bundle/{productBundleId}").hasAuthority(Role.ADMIN.name())

                        .requestMatchers(HttpMethod.GET, "/beers").permitAll()
                        .requestMatchers(HttpMethod.GET, "/beers/{id}").permitAll()
                        .requestMatchers(HttpMethod.POST, "/beers/{id}/ratings").permitAll()
                        .requestMatchers(HttpMethod.GET, "/ciders").permitAll()
                        .requestMatchers(HttpMethod.GET, "/ciders/{id}").permitAll()
                        .requestMatchers(HttpMethod.POST, "/ciders/{id}/ratings").permitAll()
                        .requestMatchers(HttpMethod.GET, "/products-bundle").permitAll()
                        .requestMatchers(HttpMethod.GET, "/products-bundle/{id}").permitAll()
                        .requestMatchers(HttpMethod.POST, "/products-bundle/{id}/ratings").permitAll()
                        .requestMatchers(HttpMethod.GET, "/snacks").permitAll()
                        .requestMatchers(HttpMethod.GET, "/snacks/{id}").permitAll()
                        .requestMatchers(HttpMethod.POST, "/snacks/{id}/ratings").permitAll()
                        .requestMatchers(HttpMethod.GET, "/special-offers/active").permitAll()

                        .requestMatchers(HttpMethod.GET, "/carts/{cartId}").permitAll()
                        .requestMatchers(HttpMethod.POST, "/carts").permitAll()
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
