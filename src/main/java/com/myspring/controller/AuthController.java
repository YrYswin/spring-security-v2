package com.myspring.controller;

import com.myspring.model.AuthResponse;
import com.myspring.model.User;
import com.myspring.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<AuthResponse> signUp(
            @RequestBody User request
            ) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/sign-in")
    public ResponseEntity<AuthResponse> signIn(
            @RequestBody User request
        ){
            return ResponseEntity.ok(authService.authenticate(request));
    }
}
