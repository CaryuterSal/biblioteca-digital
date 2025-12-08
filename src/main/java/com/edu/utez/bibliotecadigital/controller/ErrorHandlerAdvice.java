package com.edu.utez.bibliotecadigital.controller;

import com.edu.utez.bibliotecadigital.infrastructure.exceptions.NotFoundException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class ErrorHandlerAdvice {

    @ExceptionHandler(Exception.class)
    public ErrorResponse handleException(Exception ex, WebRequest request) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        log.error(ex.getMessage(), ex);
        return ErrorResponse.builder(ex, pd).build();
    }

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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleValidationExceptions(MethodArgumentNotValidException ex) {

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("Validation failed");

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        problemDetail.setProperty("errors", errors);
        return ErrorResponse.builder(ex, problemDetail).build();
    }
}
