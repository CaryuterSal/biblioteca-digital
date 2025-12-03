package com.edu.utez.bibliotecadigital.service;

import com.edu.utez.bibliotecadigital.controller.dto.LoginRequest;
import com.edu.utez.bibliotecadigital.controller.dto.TokenResponse;
import com.edu.utez.bibliotecadigital.controller.dto.UserRegisterRequest;
import com.edu.utez.bibliotecadigital.controller.dto.UserResponse;
import com.edu.utez.bibliotecadigital.model.User;
import com.edu.utez.bibliotecadigital.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UsersRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public TokenResponse register(UserRegisterRequest request) {
        User u = new User(UUID.randomUUID());
        u.setPassword(passwordEncoder.encode(request.password()));
        String token = jwtService.generateToken(userRepository.save(u).getUsername());
        return new TokenResponse(new UserResponse(u.getId(), u.getUsername()),token);
    }

    public TokenResponse authenticate(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        User userDetails =(User) userDetailsService.loadUserByUsername(request.username());
        String jwt = jwtService.generateToken(userDetails.getUsername());
        return new TokenResponse(new UserResponse(userDetails.getId(), userDetails.getUsername()),jwt);;
    }
}
