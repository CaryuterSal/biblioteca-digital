package com.edu.utez.bibliotecadigital.model;

import lombok.Data;

import java.util.UUID;

@Data
public class Book {
    private final UUID id;
    private int availableCopies;
    private String title;
    private String author;
    private boolean deleted;
}
