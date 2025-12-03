package com.edu.utez.bibliotecadigital.model;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class Loan implements Entity<UUID>, Serializable {
    private final UUID id;
    User user;
    Book book;
    LocalDate loanDate;
    LocalDate expectedReturnDate;
    LocalDate actualReturnDate;
}
