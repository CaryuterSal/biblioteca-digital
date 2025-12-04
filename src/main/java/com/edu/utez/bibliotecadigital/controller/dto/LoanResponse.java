package com.edu.utez.bibliotecadigital.controller.dto;

import com.edu.utez.bibliotecadigital.model.Book;

import java.time.LocalDate;
import java.time.Period;
import java.util.UUID;

public record LoanResponse(UUID id, UserResponse user, Book book, LocalDate loanDate, int expectedLoanDurationDays, LocalDate expectedReturnDate, LocalDate actualReturnDate, LoanStatusResponse status) {
}
