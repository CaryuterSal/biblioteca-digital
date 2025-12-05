package com.edu.utez.bibliotecadigital.controller;

import com.edu.utez.bibliotecadigital.controller.dto.LoanResponse;
import com.edu.utez.bibliotecadigital.controller.dto.UserResponse;
import com.edu.utez.bibliotecadigital.model.Book;
import com.edu.utez.bibliotecadigital.service.BooksLoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationsController {

    private final BooksLoanService booksLoanService;

    @GetMapping("/book/{id}")
    public List<LoanResponse> getPendingReservationsForBook(@PathVariable UUID id){
        return booksLoanService.findReservationsForBook(id);
    }

    @DeleteMapping
    public ResponseEntity<?> deletePendingReservations(@RequestParam UUID userId, @RequestParam UUID bookId){
        booksLoanService.deleteReservation(userId,bookId);
        return ResponseEntity.noContent().build();
    }
}
