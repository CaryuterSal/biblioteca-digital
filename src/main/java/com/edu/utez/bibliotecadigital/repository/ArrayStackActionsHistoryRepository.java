package com.edu.utez.bibliotecadigital.repository;

import com.edu.utez.bibliotecadigital.infrastructure.ArrayStack;
import com.edu.utez.bibliotecadigital.model.HistoryAction;
import com.edu.utez.bibliotecadigital.model.Loan;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ArrayStackActionsHistoryRepository implements ActionsHistoryRepository {

    private final ArrayStack<Loan> stack;

    @Override
    public HistoryAction save(HistoryAction entity) {
        return null;
    }

    @Override
    public HistoryAction findById(UUID uuid) {
        return null;
    }

    @Override
    public List<HistoryAction> findAll() {
        return List.of();
    }

    @Override
    public void delete(HistoryAction entity) {

    }

    @Override
    public void deleteById(UUID uuid) {

    }
}
