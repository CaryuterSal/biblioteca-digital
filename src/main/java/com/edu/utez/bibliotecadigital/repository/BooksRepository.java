package com.edu.utez.bibliotecadigital.repository;

import com.edu.utez.bibliotecadigital.model.Book;

import java.util.UUID;

public interface BooksRepository extends CrudRepository<Book, UUID> {
}
