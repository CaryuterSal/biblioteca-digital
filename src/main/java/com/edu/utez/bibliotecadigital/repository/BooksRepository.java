package com.edu.utez.bibliotecadigital.repository;

import com.edu.utez.bibliotecadigital.model.Book;
import com.edu.utez.bibliotecadigital.model.LoanStatus;

import java.util.Optional;
import java.util.UUID;

public interface BooksRepository extends CrudRepository<Book, UUID> {
    Optional<Book> findByTitle(String title);
}
