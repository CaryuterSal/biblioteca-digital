package com.edu.utez.bibliotecadigital.model;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class Loan {
    User user;
    Book book;
    LocalDate loanDate;
    LocalDate expectedReturnDate;
    LocalDate actualReturnDate;
}
