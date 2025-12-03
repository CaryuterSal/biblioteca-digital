package com.edu.utez.bibliotecadigital.repository;

import com.edu.utez.bibliotecadigital.model.Loan;

import java.util.UUID;

public interface LoansRepository extends CrudRepository<Loan, UUID> {
    Loan findByTitle(String title);
}
