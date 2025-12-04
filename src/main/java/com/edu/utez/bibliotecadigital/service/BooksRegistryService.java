package com.edu.utez.bibliotecadigital.service;

import com.edu.utez.bibliotecadigital.controller.dto.BookRegisterRequest;
import com.edu.utez.bibliotecadigital.infrastructure.datastructures.SinglyLinkedList;
import com.edu.utez.bibliotecadigital.infrastructure.exceptions.NotFoundException;
import com.edu.utez.bibliotecadigital.model.Book;
import com.edu.utez.bibliotecadigital.repository.BooksRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BooksRegistryService {

    private final BooksRepository booksRepository;

    public List<Book> findAll(){
        List<Book> response = new ArrayList<>();
        SinglyLinkedList<Book> books = booksRepository.findAll();
        for(int i = 0; i < books.size(); i++){
            response.add(books.get(i));
        }
        return response;
    }

    public List<Book> findActive(){
        List<Book> response = new ArrayList<>();
        SinglyLinkedList<Book> books = booksRepository.findAll();
        for(int i = 0; i < books.size(); i++){
            Book book = books.get(i);
            if(!book.isDeleted()){
                response.add(books.get(i));
            }
        }
        return response;
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
        Book book = findById(id);
        if(!book.isDeleted()){
            book.setDeleted(true);
            booksRepository.save(book);
        }
    }

    public List<Book> searchByTitle(String title){
        List<Book> response = new ArrayList<>();
        SinglyLinkedList<Book> books = booksRepository.findByTitle(title);

        for(int i = 0; i < books.size(); i++){
            response.add(books.get(i));
        }

        return response;
    }

    public Book updateStatus(UUID id){
        Book book = findById(id);
        if(book.isDeleted()){
            book.setDeleted(false);
        } else  {
            book.setDeleted(true);
        }
        return booksRepository.save(book);
    }
}
