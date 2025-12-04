package com.edu.utez.bibliotecadigital.controller.dto;

import java.time.LocalDate;
import java.util.UUID;

public record LoanRequest(UUID bookId, int daysRequesting) {
}
