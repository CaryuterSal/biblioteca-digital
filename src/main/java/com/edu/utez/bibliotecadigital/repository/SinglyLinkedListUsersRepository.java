package com.edu.utez.bibliotecadigital.repository;

import com.edu.utez.bibliotecadigital.infrastructure.datastructures.DefaultSinglyLinkedList;
import com.edu.utez.bibliotecadigital.infrastructure.datastructures.SinglyLinkedList;
import com.edu.utez.bibliotecadigital.model.Book;
import com.edu.utez.bibliotecadigital.model.User;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class SinglyLinkedListUsersRepository implements UsersRepository{


    private final ObjectProvider<@NonNull SinglyLinkedList<User>> listProvider;
    private final SinglyLinkedList<User> usersRegistry;

    @Override
    public Optional<User> findByUsername(@NonNull String username) {
        for (int i = 0; i < usersRegistry.size(); i++) {
            User current = usersRegistry.get(i);
            if (current.getUsername().equalsIgnoreCase(username)) {
                return Optional.of(current);
            }
        }
        return Optional.empty();
    }

    @Override
    public User save(User entity) {
        findById(entity.getId()).ifPresentOrElse(
                user -> {
                    delete(user);
                    usersRegistry.addLast(entity);
                },
                () -> usersRegistry.addLast(entity));
        return entity;
    }

    @Override
    public Optional<User> findById(UUID uuid) {
        for (int i = 0; i < usersRegistry.size(); i++) {
            User current = usersRegistry.get(i);
            if (current.getId().equals(uuid)) {
                return Optional.of(current);
            }
        }
        return Optional.empty();
    }

    @Override
    public SinglyLinkedList<User> findAll() {
        SinglyLinkedList<User> clone = listProvider.getObject();

        for (int i = 0; i < usersRegistry.size(); i++) {
            clone.addLast(usersRegistry.get(i));
        }

        return clone;
    }

    @Override
    public void delete(User entity) {
        deleteById(entity.getId());
    }

    @Override
    public void deleteById(UUID uuid) {
        for (int i = 0; i < usersRegistry.size(); i++) {
            User current = usersRegistry.get(i);
            if (current.getId().equals(uuid)) {
                usersRegistry.remove(current);
                return;
            }
        }
    }
}
