package com.edu.utez.bibliotecadigital.service;

import com.edu.utez.bibliotecadigital.controller.dto.*;
import com.edu.utez.bibliotecadigital.infrastructure.datastructures.ArrayStack;
import com.edu.utez.bibliotecadigital.infrastructure.datastructures.Stack;
import com.edu.utez.bibliotecadigital.model.*;
import com.edu.utez.bibliotecadigital.repository.ActionsHistoryRepository;
import com.edu.utez.bibliotecadigital.repository.BooksRepository;
import com.edu.utez.bibliotecadigital.repository.PendingLoansRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BooksLoanService {

    private final BooksRegistryService booksService;
    private final BooksRepository booksRepository;
    private final UserService userService;
    private final PendingLoansRepository pendingLoansRepository;
    private final ActionsHistoryRepository actionsHistoryRepository;


    public LoanResponse createLoan(LoanRequest request){

        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Book book = booksService.findById(request.bookId());
        if(book.getAvailableCopies() <= 0){
            LoanStatus loanStatus = LoanStatus.createWaiting(
                    UUID.randomUUID(),
                    currentUser,
                    book,
                    Period.ofDays(request.daysRequesting()));
            loanStatus = pendingLoansRepository.save(loanStatus);

            book.incrementStock();
            booksRepository.save(book);

            return new LoanResponse(
                    new UserResponse(loanStatus.getUser().getId(), loanStatus.getUser().getUsername()),
                    loanStatus.getBook(),
                    loanStatus.getLoanDate(),
                    loanStatus.getExpectedLoanPeriod().getDays(),
                    null,
                    loanStatus.getActualReturnDate(),
                    LoanStatusResponse.PENDING);
        } else {
            LoanStatus loanStatus = LoanStatus.createGranted(
                    UUID.randomUUID(),
                    currentUser,
                    book,
                    Period.ofDays(request.daysRequesting())
            );
            loanStatus = actionsHistoryRepository.save(loanStatus);
            book.incrementStock();
            booksRepository.save(book);
            return new LoanResponse(
                    new UserResponse(loanStatus.getUser().getId(), loanStatus.getUser().getUsername()),
                    loanStatus.getBook(),
                    loanStatus.getLoanDate(),
                    loanStatus.getExpectedLoanPeriod().getDays(),
                    loanStatus.getExpectedReturnDate(),
                    loanStatus.getActualReturnDate(),
                    LoanStatusResponse.GRANTED);
        }
    }

    public Optional<LoanStatus> returnLoaned(LoanReturnRequest request){
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Book book = booksService.findById(request.bookId());
        Stack<LoanStatus> loans = actionsHistoryRepository.findActiveLoansForUser(currentUser.getId());

        while(!loans.isEmpty()){
            LoanStatus topLoan =  loans.pop();
            if(topLoan.getBook().getId().equals(book.getId())){
                book.incrementStock();
                booksRepository.save(book);

                LoanStatus returnLoanStatus = topLoan.returnLoan();
                actionsHistoryRepository.save(returnLoanStatus);
                return Optional.of(returnLoanStatus);
            }
        }

        return Optional.empty();
    }

    public List<LoanStatus> findLoansForUser(UUID userId){
        boolean isAdmin =  SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!(isAdmin || currentUser.getId().equals(userId))) throw new AuthorizationDeniedException("Cannot query loans for external users");

        List<LoanStatus> response = new ArrayList<>();
        Stack<LoanStatus> loans = actionsHistoryRepository.findActiveLoansForUser(userId);

        while(!loans.isEmpty()){
            response.add(loans.pop());
        }

        return response;
    }
}
