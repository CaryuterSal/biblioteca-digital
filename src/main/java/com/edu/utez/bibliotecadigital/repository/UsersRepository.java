package com.edu.utez.bibliotecadigital.repository;

import com.edu.utez.bibliotecadigital.model.User;
import org.jspecify.annotations.NonNull;

public interface UsersRepository {
    User findByUsername(@NonNull String username);
}
