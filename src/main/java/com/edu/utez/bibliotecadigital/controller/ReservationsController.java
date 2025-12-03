package com.edu.utez.bibliotecadigital.controller;

import com.edu.utez.bibliotecadigital.controller.dto.UserResponse;
import com.edu.utez.bibliotecadigital.model.Book;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/reservations")
public class ReservationsController {

    @GetMapping("/book/{id}")
    public List<UserResponse> getPendingReservations(@PathVariable String id){
        return null;
    }

    @DeleteMapping
    public ResponseEntity<?> deletePendingReservations(@RequestParam UUID userId, @RequestParam UUID bookId){
        return null;
    }
}
