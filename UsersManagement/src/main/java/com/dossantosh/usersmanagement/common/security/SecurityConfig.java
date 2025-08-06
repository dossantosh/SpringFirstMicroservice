package com.dossantosh.usersmanagement.common.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.dossantosh.usersmanagement.common.global.errors.custom.RestAccessDeniedHandler;
import com.dossantosh.usersmanagement.common.global.errors.custom.RestAuthenticationEntryPoint;
import com.dossantosh.usersmanagement.common.security.custom.auth.bus.CustomUserDetailsService;
import com.dossantosh.usersmanagement.common.security.jwt.JwtAuthFilter;

import java.util.List;

/**
 * Configuration class for Spring Security.
 * 
 * Defines security filter chain, CORS configuration, password encoding,
 * and authentication manager beans.
 */
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final CustomUserDetailsService customUserDetailsService;

    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    private final RestAccessDeniedHandler restAccessDeniedHandler;

    /**
     * Configures the security filter chain.
     * Disables CSRF, enables CORS, configures request authorization,
     * disables session creation (stateless), sets JWT filter,
     * and sets custom handlers for unauthorized and access denied errors.
     * 
     * @param http HttpSecurity object
     * @return configured SecurityFilterChain
     * @throws Exception if configuration fails
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers("/api/**").permitAll()
                .anyRequest().authenticated())
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .securityContext(securityContext -> securityContext.requireExplicitSave(false))
            .userDetailsService(customUserDetailsService)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint(restAuthenticationEntryPoint)   // Handles 401 Unauthorized
                .accessDeniedHandler(restAccessDeniedHandler)             // Handles 403 Forbidden
            )
            .formLogin(form -> form.disable())  // Disable default form login
            .httpBasic(AbstractHttpConfigurer::disable); // Disable HTTP Basic auth

        return http.build();
    }

    /**
     * Defines the CORS configuration source for handling cross-origin requests.
     * Allows requests from http://localhost:4200 with common HTTP methods.
     * 
     * @return CorsConfigurationSource bean
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(false);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * Exposes AuthenticationManager bean from AuthenticationConfiguration.
     * 
     * @param config AuthenticationConfiguration injected by Spring
     * @return AuthenticationManager bean
     * @throws Exception if retrieval fails
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Defines the PasswordEncoder bean using BCrypt hashing algorithm.
     * 
     * @return PasswordEncoder instance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
