package com.edu.utez.bibliotecadigital.repository;
import com.edu.utez.bibliotecadigital.infrastructure.datastructures.Stack;
import com.edu.utez.bibliotecadigital.model.LoanStatus;
import com.edu.utez.bibliotecadigital.model.TypeAction;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface ActionsHistoryRepository extends CrudRepository<LoanStatus, UUID> {
    Optional<LoanStatus> pop();
    Optional<LoanStatus> peek();
    Optional<LoanStatus> peekForUser(UUID userId);
    Optional<LoanStatus> popForUser(UUID userId);
    Stack<LoanStatus> findForUser(UUID userId);
    Stack<LoanStatus> findActiveLoansForUser(UUID userId);

    Optional<LoanStatus> popForUserByAction(UUID userId, TypeAction typeAction);
    Optional<LoanStatus> popByAction(TypeAction typeAction);
}
