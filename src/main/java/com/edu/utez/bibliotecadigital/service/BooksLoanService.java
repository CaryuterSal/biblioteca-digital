package com.edu.utez.bibliotecadigital.service;

import com.edu.utez.bibliotecadigital.controller.dto.*;
import com.edu.utez.bibliotecadigital.infrastructure.datastructures.ArrayStack;
import com.edu.utez.bibliotecadigital.infrastructure.datastructures.Queue;
import com.edu.utez.bibliotecadigital.infrastructure.datastructures.Stack;
import com.edu.utez.bibliotecadigital.infrastructure.exceptions.NotFoundException;
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

    public Optional<LoanResponse> returnLoaned(LoanReturnRequest request){
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Book book = booksService.findById(request.bookId());
        Stack<LoanStatus> loans = actionsHistoryRepository.findActiveLoansForUser(currentUser.getId());

        while(!loans.isEmpty()){
            LoanStatus topLoan =  loans.pop();
            if(topLoan.getBook().getId().equals(book.getId())){

                LoanStatus returnLoanStatus = topLoan.returnLoan();
                actionsHistoryRepository.save(returnLoanStatus);

                Queue<LoanStatus> pending = pendingLoansRepository.findPendingLoansForBook(book.getId());

                if (!pending.isEmpty()) {
                    LoanStatus waiting = pending.dequeue();
                    pendingLoansRepository.delete(waiting);

                    LoanStatus newLoan = LoanStatus.createGranted(
                            UUID.randomUUID(),
                            waiting.getUser(),
                            book,
                            waiting.getExpectedLoanPeriod()
                    );

                    actionsHistoryRepository.save(newLoan);
                } else {

                    book.incrementStock();
                    booksRepository.save(book);
                }

                return Optional.of(new LoanResponse(
                        new UserResponse(returnLoanStatus.getUser().getId(), returnLoanStatus.getUser().getUsername()),
                        returnLoanStatus.getBook(),
                        returnLoanStatus.getLoanDate(),
                        returnLoanStatus.getExpectedLoanPeriod().getDays(),
                        returnLoanStatus.getExpectedReturnDate(),
                        returnLoanStatus.getActualReturnDate(),
                        LoanStatusResponse.RETURNED));
            }
        }

        return Optional.empty();
    }

    public List<LoanResponse> findLoansForUser(UUID userId){
        boolean isAdmin =  SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!(isAdmin || currentUser.getId().equals(userId))) throw new AuthorizationDeniedException("Cannot query loans for external users");

        List<LoanResponse> response = new ArrayList<>();
        Stack<LoanStatus> loans = actionsHistoryRepository.findActiveLoansForUser(userId);

        while(!loans.isEmpty()){
            LoanStatus topLoan = loans.pop();
            response.add(new LoanResponse(
                    new UserResponse(topLoan.getUser().getId(), topLoan.getUser().getUsername()),
                    topLoan.getBook(),
                    topLoan.getLoanDate(),
                    topLoan.getExpectedLoanPeriod().getDays(),
                    topLoan.getExpectedReturnDate(),
                    topLoan.getActualReturnDate(),
                    LoanStatusResponse.RETURNED));
        }

        return response;
    }

    public List<LoanResponse> findReservationsForBook(UUID bookId){
        Queue<LoanStatus> pending = pendingLoansRepository.findPendingLoansForBook(bookId);
        List<LoanResponse> result = new ArrayList<>();

        while (!pending.isEmpty()) {
            LoanStatus ls = pending.dequeue();

            result.add(new LoanResponse(
                    new UserResponse(ls.getUser().getId(), ls.getUser().getUsername()),
                    ls.getBook(),
                    ls.getLoanDate(),
                    ls.getExpectedLoanPeriod().getDays(),
                    null,
                    ls.getActualReturnDate(),
                    LoanStatusResponse.PENDING
            ));
        }

        return result;
    }

    public void deleteReservation(UUID bookId, UUID userId){
        Queue<LoanStatus> pending = pendingLoansRepository.findPendingLoansForBook(bookId);

        while (!pending.isEmpty()) {
            LoanStatus ls = pending.dequeue();
            if (ls.getUser().getId().equals(userId)) {
                pendingLoansRepository.delete(ls);
                return;
            }
        }

        throw new NotFoundException("No reservation found for the provided user/book");

    }
}
