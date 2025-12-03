package com.edu.utez.bibliotecadigital.controller;

import com.edu.utez.bibliotecadigital.controller.dto.BookRegisterRequest;
import com.edu.utez.bibliotecadigital.controller.dto.BookStatusUpdateRequest;
import com.edu.utez.bibliotecadigital.model.Book;
import jakarta.validation.Valid;
import lombok.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/books")
public class BooksController {

    @PostMapping
    public ResponseEntity<@NonNull Book> addBook(@Valid @RequestBody BookRegisterRequest dto){
return null;
    }

    @GetMapping
    public List<Book> getAllBooks() {
        return null;
    }

    @GetMapping("/{id}")
    public Book getBookById(@PathVariable UUID id){
        return null;
    }

    @PutMapping("/{id}")
    public Book updateBookById(@PathVariable UUID id, @Valid @RequestBody BookRegisterRequest dto){
        return null;
    }

    @PatchMapping("/{id}")
    public Book updateBookStatus(@Valid @RequestBody BookStatusUpdateRequest request, @PathVariable UUID id){
        return null;
    }

    @GetMapping("/search")
    public List<Book> searchBooks(@RequestParam String title){
        return null;
    }

}
