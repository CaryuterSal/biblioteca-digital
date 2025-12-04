package com.edu.utez.bibliotecadigital.controller;

import com.edu.utez.bibliotecadigital.controller.dto.BookRegisterRequest;
import com.edu.utez.bibliotecadigital.controller.dto.BookStatusUpdateRequest;
import com.edu.utez.bibliotecadigital.model.Book;
import com.edu.utez.bibliotecadigital.service.BooksRegistryService;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BooksController {

    private final BooksRegistryService booksRegistryService;


    @PostMapping
    public ResponseEntity<@NonNull Book> addBook(@Valid @RequestBody BookRegisterRequest dto){
        Book book = booksRegistryService.create(dto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(book.getId())
                .toUri();
        return ResponseEntity.created(location)
                .body(book);
    }

    @GetMapping
    public List<Book> getAllBooks() {
        return booksRegistryService.findAll();
    }

    @GetMapping("/{id}")
    public Book getBookById(@PathVariable UUID id){
        return booksRegistryService.findById(id);
    }

    @PutMapping("/{id}")
    public Book updateBookById(@PathVariable UUID id, @Valid @RequestBody BookRegisterRequest dto){
        return booksRegistryService.updateById(id, dto);
    }

    @PatchMapping("/{id}")
    public Book updateBookStatus(@PathVariable UUID id){
        return booksRegistryService.updateStatus(id);
    }

    @GetMapping("/search")
    public List<Book> searchBooks(@RequestParam String title){
        return booksRegistryService.searchByTitle(title);
    }

}
