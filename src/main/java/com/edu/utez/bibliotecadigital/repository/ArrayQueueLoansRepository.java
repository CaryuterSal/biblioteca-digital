package com.edu.utez.bibliotecadigital.repository;

import com.edu.utez.bibliotecadigital.infrastructure.ArrayQueue;
import com.edu.utez.bibliotecadigital.model.Loan;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ArrayQueueLoansRepository implements LoansRepository{

    private final ArrayQueue<Loan> queue;

    @Override
    public Loan findByTitle(String title) {
        return null;
    }

    @Override
    public Loan save(Loan entity) {
        return null;
    }

    @Override
    public Loan findById(UUID uuid) {
        return null;
    }

    @Override
    public List<Loan> findAll() {
        return List.of();
    }

    @Override
    public void delete(Loan entity) {

    }

    @Override
    public void deleteById(UUID uuid) {

    }
}
