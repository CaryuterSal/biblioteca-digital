package com.edu.utez.bibliotecadigital.repository;

import com.edu.utez.bibliotecadigital.infrastructure.datastructures.DefaultSinglyLinkedList;
import com.edu.utez.bibliotecadigital.infrastructure.datastructures.SinglyLinkedList;
import com.edu.utez.bibliotecadigital.model.Book;
import com.edu.utez.bibliotecadigital.model.LoanStatus;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class SinglyLinkedListBooksRepository implements BooksRepository{

    private final ObjectProvider<@NonNull SinglyLinkedList<Book>> listProvider;
    private final SinglyLinkedList<Book> booksRegistry;


    @Override
    public Book save(Book entity) {
        booksRegistry.addLast(entity);
        return entity;
    }

    @Override
    public Optional<Book> findById(UUID uuid) {
        for (int i = 0; i < booksRegistry.size(); i++) {
            Book current = booksRegistry.get(i);
            if (current.getId().equals(uuid)) {
                return Optional.of(current);
            }
        }
        return Optional.empty();
    }

    @Override
    public SinglyLinkedList<Book> findAll() {
        SinglyLinkedList<Book> clone = listProvider.getObject();

        for (int i = 0; i < booksRegistry.size(); i++) {
            clone.addLast(booksRegistry.get(i));
        }

        return clone;
    }

    @Override
    public void delete(Book entity) {
        for (int i = 0; i < booksRegistry.size(); i++) {
            Book current = booksRegistry.get(i);
            if (current.equals(entity)) {
                booksRegistry.remove(current);
                return;
            }
        }
    }

    @Override
    public void deleteById(UUID uuid) {
        for (int i = 0; i < booksRegistry.size(); i++) {
            Book current = booksRegistry.get(i);
            if (current.getId().equals(uuid)) {
                booksRegistry.remove(current);
                return;
            }
        }
    }

    @Override
    public Optional<Book> findByTitle(String title) {
        for (int i = 0; i < booksRegistry.size(); i++) {
            Book book = booksRegistry.get(i);
            if (book.getTitle().equalsIgnoreCase(title)) {
                return Optional.of(book);
            }
        }
        return Optional.empty();
    }
}
