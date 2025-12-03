package com.edu.utez.bibliotecadigital.controller;

import com.edu.utez.bibliotecadigital.controller.dto.LoginRequest;
import com.edu.utez.bibliotecadigital.controller.dto.TokenResponse;
import com.edu.utez.bibliotecadigital.controller.dto.UserRegisterRequest;
import com.edu.utez.bibliotecadigital.model.Loan;
import com.edu.utez.bibliotecadigital.service.AuthService;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping
    public ResponseEntity<@NonNull TokenResponse> register(@Valid @RequestBody UserRegisterRequest request) {
        TokenResponse tokenResponse = authService.register(request);
        URI location = MvcUriComponentsBuilder
                .fromController(UsersController.class)
                .path("/{id}")
                .buildAndExpand(tokenResponse.userInfo().id())
                .toUri();
        return ResponseEntity.created(location)
                .body(tokenResponse);
    }

    @PostMapping
    public TokenResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.authenticate(request);
    }
}
