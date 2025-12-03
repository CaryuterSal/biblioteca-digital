package com.edu.utez.bibliotecadigital.controller;

import com.edu.utez.bibliotecadigital.controller.dto.BookRegisterRequest;
import com.edu.utez.bibliotecadigital.model.Book;
import lombok.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/books")
public class BooksController {

    @PostMapping
    public ResponseEntity<@NonNull Book> addBook(@RequestBody BookRegisterRequest dto){

    }

    @GetMapping
    public R
}
