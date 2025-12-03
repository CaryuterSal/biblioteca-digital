package com.edu.utez.bibliotecadigital.controller.dto;

import jakarta.validation.constraints.NotNull;

public record BookStatusUpdateRequest(@NotNull Boolean active) {
}
