package com.edu.utez.bibliotecadigital.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

public record HistoryAction(UUID id,  User subject, Book book, TypeAction typeAction, LocalDateTime createdAt) implements Entity<UUID>{

    @Override
    public UUID getId() {
        return id;
    }
}
