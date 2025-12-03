package com.edu.utez.bibliotecadigital.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record LoginRequest(@NotBlank String username,
                           @NotBlank String password) {
}
