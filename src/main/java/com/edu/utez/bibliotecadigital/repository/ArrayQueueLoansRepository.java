package com.edu.utez.bibliotecadigital.repository;

import com.edu.utez.bibliotecadigital.infrastructure.ArrayQueue;
import com.edu.utez.bibliotecadigital.model.Loan;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ArrayQueueLoansRepository implements LoansRepository{

    private final ArrayQueue<Loan> queue;

}
