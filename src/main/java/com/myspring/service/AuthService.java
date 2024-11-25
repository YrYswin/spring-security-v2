package com.myspring.service;

import com.myspring.filter.JwtAuthFilter;
import com.myspring.model.AuthResponse;
import com.myspring.model.User;
import com.myspring.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.webauthn.api.AuthenticatorResponse;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository repository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager manager) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        authenticationManager = manager;
    }

    public AuthResponse register(User request) {
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user.setRole(request.getRole());

        user = repository.save(user);

        String token = jwtService.generateToken(user);

        return new AuthResponse(token);
    }

    public AuthResponse authenticate(User request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        User user = repository.findByUsername(request.getUsername()).orElseThrow();
        String token = jwtService.generateToken(user);

        return new AuthResponse(token);
    }
}
