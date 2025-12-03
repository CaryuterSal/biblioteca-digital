package com.edu.utez.bibliotecadigital.repository;

import com.edu.utez.bibliotecadigital.infrastructure.SinglyLinkedList;
import com.edu.utez.bibliotecadigital.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SinglyLinkedListUsersRepository implements UsersRepository{

    private final SinglyLinkedList<User> list;
}
