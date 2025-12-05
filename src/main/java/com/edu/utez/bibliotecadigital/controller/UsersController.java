package com.edu.utez.bibliotecadigital.controller;

import com.edu.utez.bibliotecadigital.controller.dto.UserResponse;
import com.edu.utez.bibliotecadigital.service.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UsersController {

    private final UserService userService;

    @GetMapping
    public List<UserResponse> getAllUsers(){
        return userService.findUsers();
    }

    @GetMapping("/{id}")
    public UserResponse getUser(@PathVariable UUID id){
        return userService.findUser(id);
    }
}
