package com.edu.utez.bibliotecadigital.controller.dto;

import com.edu.utez.bibliotecadigital.model.Book;

import java.time.LocalDate;
import java.time.Period;

public record LoanResponse(UserResponse user, Book book, LocalDate loanDate, int expectedLoanDurationDays, LocalDate expectedReturnDate, LocalDate actualReturnDate, LoanStatusResponse status) {
}
