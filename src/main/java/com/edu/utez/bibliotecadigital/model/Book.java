package com.edu.utez.bibliotecadigital.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class Book extends Entity<UUID> implements Serializable {
    private final UUID id;
    private int availableCopies;
    private String title;
    private String author;
    private boolean deleted = false;

    public Book(UUID id, int availableCopies, String title, String author) {
        this.id = id;
        this.availableCopies = availableCopies;
        this.title = title;
        this.author = author;
    }

    public void incrementStock(){
        availableCopies++;
    }

    public void decrementStock(){
        availableCopies--;
    }
}
