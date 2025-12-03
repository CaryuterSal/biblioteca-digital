package com.edu.utez.bibliotecadigital.model;

import lombok.Data;

import java.util.UUID;

@Data
public class User {
    private final UUID id;
    private String username;
    private String password;
}
