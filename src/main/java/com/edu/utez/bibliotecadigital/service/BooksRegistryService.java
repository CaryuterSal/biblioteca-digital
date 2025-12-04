package com.edu.utez.bibliotecadigital.service;

import com.edu.utez.bibliotecadigital.controller.dto.BookRegisterRequest;
import com.edu.utez.bibliotecadigital.infrastructure.exceptions.NotFoundException;
import com.edu.utez.bibliotecadigital.model.Book;
import com.edu.utez.bibliotecadigital.repository.BooksRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BooksRegistryService {

    private final BooksRepository booksRepository;

    public List<Book> findAll(){
        return booksRepository.findAll();
    }

    public List<Book> findActive(){
        return booksRepository.findAll().stream()
                .filter(book -> !book.isDeleted())
                .toList();
    }

    public Book findById(UUID id){
        return booksRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Book.class, id));
    }

    public void updateById(UUID id, BookRegisterRequest request){
        Book oldBook = findById(id);
        oldBook.setTitle(request.title());
        oldBook.setAuthor(request.author());
        oldBook.setAvailableCopies(request.availableCopies());
        booksRepository.save(oldBook);
    }

    public void create(BookRegisterRequest request){
        Book book = new Book(UUID.randomUUID(), request.availableCopies(), request.title(), request.author());
        booksRepository.save(book);
    }

    public void deleteById(UUID id){
        booksRepository.deleteById(id);
    }
}
