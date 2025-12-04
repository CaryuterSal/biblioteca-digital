package com.edu.utez.bibliotecadigital.model;

import lombok.*;
import org.springframework.cglib.core.Local;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.TemporalUnit;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LoanStatus extends Entity<UUID> implements Serializable{

    private final UUID id;
    private User user;
    private Book book;
    private @With LocalDate loanDate;
    private Period expectedLoanPeriod;
    private LocalDate actualReturnDate;
    private @With TypeAction typeAction;

    public static LoanStatus createWaiting(UUID id, User user, Book book, Period expectedLoanPeriod){
        return new LoanStatus(
                id,
                user,
                book,
                null,
                expectedLoanPeriod,
                null,
                TypeAction.ADD_TO_WAITLIST
        );
    }

    public static LoanStatus createGranted(UUID id, User user, Book book, Period expectedLoanPeriod){
        return new LoanStatus(
                id,
                user,
                book,
                LocalDate.now(),
                expectedLoanPeriod,
                null,
                TypeAction.CREATE_LOAN
        );
    }

    public LoanStatus asGranted(){
        return this
                .withLoanDate(LocalDate.now())
                .withTypeAction(TypeAction.CREATE_LOAN);
    }

    public LoanStatus returnLoan(){
        return this.withTypeAction(TypeAction.RETURN_LOAN);
    }

    @Override
    public UUID getId() {
        return id;
    }

    public LocalDate getExpectedReturnDate(){
        if(this.loanDate == null){
            throw new IllegalStateException("Not granted yet");
        }
        return loanDate.plus(expectedLoanPeriod);
    }
}
