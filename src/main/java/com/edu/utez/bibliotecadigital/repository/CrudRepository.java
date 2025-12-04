package com.edu.utez.bibliotecadigital.repository;

import com.edu.utez.bibliotecadigital.infrastructure.datastructures.SinglyLinkedList;
import com.edu.utez.bibliotecadigital.model.Entity;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface CrudRepository<T extends Entity<ID>, ID extends Serializable> {
    T save(T entity);
    Optional<T> findById(ID id);
    SinglyLinkedList<T> findAll();
    void delete(T entity);
    void deleteById(ID id);

}
