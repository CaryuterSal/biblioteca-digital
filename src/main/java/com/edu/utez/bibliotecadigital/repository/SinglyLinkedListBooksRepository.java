package com.edu.utez.bibliotecadigital.repository;

import com.edu.utez.bibliotecadigital.infrastructure.SinglyLinkedList;
import com.edu.utez.bibliotecadigital.model.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SinglyLinkedListBooksRepository implements BooksRepository{

    private final SinglyLinkedList<Book> list;
}
