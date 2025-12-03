package com.edu.utez.bibliotecadigital.controller.dto;

import com.edu.utez.bibliotecadigital.model.Book;

import java.time.LocalDate;

public record LoanResponse(UserResponse user, Book book, LocalDate loanDate, LocalDate expectedReturnDate, LocalDate actualReturnDate) {
}
