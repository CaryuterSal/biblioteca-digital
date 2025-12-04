package com.edu.utez.bibliotecadigital.controller;

import com.edu.utez.bibliotecadigital.infrastructure.exceptions.NotFoundException;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandlerAdvice {

    @ExceptionHandler({AuthenticationException.class, AuthorizationDeniedException.class})
    public ErrorResponse handleAuthenticationException(Exception e) {
        return ErrorResponse.builder(e,
                ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED))
                .title("Not Authorized")
                .detail("Invalid login credentials")
                .build();
    }

    @ExceptionHandler(NotFoundException.class)
    public ErrorResponse handleNotFoundException(NotFoundException e) {
        return ErrorResponse.builder(e,
                        ProblemDetail.forStatus(HttpStatus.NOT_FOUND))
                .title("Not Found")
                .detail(e.getMessage())
                .build();
    }
}
