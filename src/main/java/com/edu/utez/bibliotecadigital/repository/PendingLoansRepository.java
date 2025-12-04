package com.edu.utez.bibliotecadigital.repository;

import com.edu.utez.bibliotecadigital.infrastructure.datastructures.Queue;
import com.edu.utez.bibliotecadigital.model.LoanStatus;

import java.util.Optional;
import java.util.UUID;

public interface PendingLoansRepository extends CrudRepository<LoanStatus, UUID> {
    Optional<LoanStatus> poll();
    Optional<LoanStatus> pollForUser(UUID userId);
    Optional<LoanStatus> peekForUser(UUID userId);
    Optional<LoanStatus> peek();

    Queue<LoanStatus> findPendingLoansForBook(UUID bookId);
}
