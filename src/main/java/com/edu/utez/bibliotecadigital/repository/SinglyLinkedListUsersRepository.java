package com.edu.utez.bibliotecadigital.repository;

import com.edu.utez.bibliotecadigital.infrastructure.SinglyLinkedList;
import com.edu.utez.bibliotecadigital.model.User;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class SinglyLinkedListUsersRepository implements UsersRepository{

    private final SinglyLinkedList<User> list;

    @Override
    public User findByUsername(@NonNull String username) {
        return null;
    }

    @Override
    public User save(User entity) {
        return null;
    }

    @Override
    public User findById(UUID uuid) {
        return null;
    }

    @Override
    public List<User> findAll() {
        return List.of();
    }

    @Override
    public void delete(User entity) {

    }

    @Override
    public void deleteById(UUID uuid) {

    }
}
