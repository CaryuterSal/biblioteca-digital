package com.edu.utez.bibliotecadigital.repository;

import com.edu.utez.bibliotecadigital.infrastructure.SinglyLinkedList;
import com.edu.utez.bibliotecadigital.model.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class SinglyLinkedListBooksRepository implements BooksRepository{

    private final SinglyLinkedList<Book> list;

    @Override
    public Book save(Book entity) {
        return null;
    }

    @Override
    public Book findById(UUID uuid) {
        return null;
    }

    @Override
    public List<Book> findAll() {
        return List.of();
    }

    @Override
    public void delete(Book entity) {

    }

    @Override
    public void deleteById(UUID uuid) {

    }
}
