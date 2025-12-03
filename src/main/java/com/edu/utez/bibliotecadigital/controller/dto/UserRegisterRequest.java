package com.edu.utez.bibliotecadigital.controller.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserRegisterRequest(
        @NotBlank String username,
        @NotBlank
        @Size(min = 6, message = "Password must have at least 6 characters")
        @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{6,20}$",
                message = "Password must contain at least one digit, one lowercase, one uppercase, and one special character")
        String password) {
}
