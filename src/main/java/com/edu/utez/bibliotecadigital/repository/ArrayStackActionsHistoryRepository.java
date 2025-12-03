package com.edu.utez.bibliotecadigital.repository;

import com.edu.utez.bibliotecadigital.infrastructure.ArrayStack;
import com.edu.utez.bibliotecadigital.model.Loan;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ArrayStackActionsHistoryRepository implements ActionsHistoryRepository {

    private final ArrayStack<Loan> stack;
}
