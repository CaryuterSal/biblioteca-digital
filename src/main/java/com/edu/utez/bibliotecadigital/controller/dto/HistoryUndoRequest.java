package com.edu.utez.bibliotecadigital.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.Optional;

public record HistoryUndoRequest(
        @Positive Optional<Integer> steps,
        @NotNull HistoryUndoAction action) {
}
