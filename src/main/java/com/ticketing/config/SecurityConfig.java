package com.ticketing.config;

import com.ticketing.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * SECURITY DESIGN DECISIONS:
 * ==========================
 * 1. STATELESS SESSION: We use stateless sessions (SessionCreationPolicy.STATELESS) because JWT is self-contained. 
 *    The server doesn't store user session state, allowing clean scaling and decoupled containerized nodes.
 * 2. CSRF DISABLED: Cross-Site Request Forgery is disabled because our REST API is stateless and doesn't rely 
 *    on cookies for session verification. Tokens must be explicitly sent in authorization headers.
 * 3. METHOD SECURITY: Enabled via @EnableMethodSecurity(prePostEnabled = true) to secure individual service or 
 *    controller routes using role check annotations like @PreAuthorize("hasRole('ADMIN')").
 *
 * JWT AUTHENTICATION FLOW:
 * ========================
 * 1. Client sends POST /api/auth/login with email + password
 * 2. AuthServiceImpl authenticates via AuthenticationManager
 * 3. On success, JwtTokenProvider generates a signed JWT token
 * 4. Client stores the token (localStorage / memory)
 * 5. Every subsequent request includes header:
 *       Authorization: Bearer <token>
 * 6. JwtAuthenticationFilter intercepts the request
 * 7. Extracts and validates the token
 * 8. Loads UserDetails, sets Authentication in SecurityContextHolder
 * 9. Spring Security allows the request to proceed
 * 10. @PreAuthorize annotations check roles at method level
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/register", "/api/auth/login").permitAll()
                .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/api-docs/**").permitAll()
                .requestMatchers("/actuator/health").permitAll()
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOriginPattern("*"); // Allow all origins in dev
        configuration.addAllowedMethod("*"); // Allow GET, POST, PUT, DELETE, OPTIONS
        configuration.addAllowedHeader("*"); // Allow all headers
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
