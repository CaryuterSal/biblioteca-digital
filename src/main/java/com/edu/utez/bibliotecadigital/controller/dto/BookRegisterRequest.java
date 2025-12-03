package com.edu.utez.bibliotecadigital.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record BookRegisterRequest(
        @Positive  int availableCopies,
        @NotBlank String title,
        @NotBlank String author) {
}
