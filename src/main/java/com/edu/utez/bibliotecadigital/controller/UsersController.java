package com.edu.utez.bibliotecadigital.controller;

import com.edu.utez.bibliotecadigital.controller.dto.UserResponse;
import lombok.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UsersController {
    @GetMapping
    public List<UserResponse> getAllUsers(){
        return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity<@NonNull UserResponse> getUser(@PathVariable UUID id){
        return null;
    }
}
