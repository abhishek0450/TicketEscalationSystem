package com.ticketing.service.impl;

import com.ticketing.dto.request.LoginRequest;
import com.ticketing.dto.request.RegisterRequest;
import com.ticketing.dto.response.AuthResponse;
import com.ticketing.entity.Role;
import com.ticketing.entity.User;
import com.ticketing.entity.enums.RoleName;
import com.ticketing.exception.ResourceAlreadyExistsException;
import com.ticketing.repository.RoleRepository;
import com.ticketing.repository.UserRepository;
import com.ticketing.security.JwtTokenProvider;
import com.ticketing.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResourceAlreadyExistsException("Email already exists: " + request.getEmail());
        }

        String roleStr = request.getRole();
        if (roleStr == null || roleStr.trim().isEmpty()) {
            roleStr = "ROLE_EMPLOYEE";
        }

        RoleName roleName;
        try {
            roleName = RoleName.valueOf(roleStr);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid role specified: " + roleStr);
        }

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role " + roleName + " not found in database."));

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .department(request.getDepartment())
                .isActive(true)
                .roles(List.of(role))
                .build();

        userRepository.save(user);

        String token = tokenProvider.generateToken(user);
        log.info("New user registered: {} with role {}", user.getEmail(), role.getName().name());

        return AuthResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .userId(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(role.getName().name())
                .expiresIn(jwtExpiration)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + request.getEmail()));

        String token = tokenProvider.generateToken(user);
        String primaryRole = user.getRoles().isEmpty() ? "ROLE_EMPLOYEE" : user.getRoles().get(0).getName().name();

        log.info("User logged in: {}", user.getEmail());

        return AuthResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .userId(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(primaryRole)
                .expiresIn(jwtExpiration)
                .build();
    }
}
