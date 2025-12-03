package com.edu.utez.bibliotecadigital.model;

import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
public class Book implements Entity<UUID>, Serializable {
    private final UUID id;
    private int availableCopies;
    private String title;
    private String author;
    private boolean deleted;
}
