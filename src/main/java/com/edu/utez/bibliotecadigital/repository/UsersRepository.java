package com.edu.utez.bibliotecadigital.repository;

import com.edu.utez.bibliotecadigital.model.User;
import org.jspecify.annotations.NonNull;

import java.util.Optional;
import java.util.UUID;

public interface UsersRepository extends CrudRepository<User, UUID> {
    Optional<User> findByUsername(@NonNull String username);

}
