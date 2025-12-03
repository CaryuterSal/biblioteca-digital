package com.edu.utez.bibliotecadigital.controller;

import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandlerAdvice {

    @ExceptionHandler(AuthenticationException.class)
    public ErrorResponse handleAuthenticationException(AuthenticationException e) {
        return ErrorResponse.builder(e,
                ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED))
                .title("Not Authorized")
                .detail("Invalid login credentials")
                .build();
    }
}
