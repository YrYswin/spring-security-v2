package com.myspring.service;

import com.myspring.filter.JwtAuthFilter;
import com.myspring.model.AuthResponse;
import com.myspring.model.Token;
import com.myspring.model.User;
import com.myspring.repository.TokenRepository;
import com.myspring.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.webauthn.api.AuthenticatorResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;

    public AuthService(UserRepository repository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager manager, TokenRepository tokenRepository) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        authenticationManager = manager;
        this.tokenRepository = tokenRepository;
    }

    public AuthResponse register(User request) {
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user.setRole(request.getRole());

        user = repository.save(user);

        String jwt = jwtService.generateToken(user);

        // save the generate token
        saveUserToken(jwt, user);

        return new AuthResponse(jwt);
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

        revokeUserToken(user);

        saveUserToken(token, user);

        return new AuthResponse(token);
    }

    private void revokeUserToken(User user) {
        List<Token> validTokenListByUser = tokenRepository.findAllTokenByUser(user.getId());

        if (!validTokenListByUser.isEmpty()) {
           validTokenListByUser.forEach(t -> t
                   .setLoggedOut(true)
           );
        }

        tokenRepository.saveAll(validTokenListByUser);
    }

    private void saveUserToken(String jwt, User user) {
        Token token = new Token();
        token.setToken(jwt);
        token.setLoggedOut(false);
        token.setUser(user);
        tokenRepository.save(token);
    }
}
