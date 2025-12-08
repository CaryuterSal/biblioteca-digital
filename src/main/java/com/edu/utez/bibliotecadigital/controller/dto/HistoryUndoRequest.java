package com.edu.utez.bibliotecadigital.controller.dto;

import com.edu.utez.bibliotecadigital.model.TypeAction;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.Optional;

public record HistoryUndoRequest(
        Optional<@Positive Integer> steps,
        Optional<TypeAction> action) {
}
